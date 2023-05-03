package com.joizhang.chat.gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.joizhang.chat.gateway.config.GatewayConfigProperties;
import com.joizhang.chat.gateway.loadbalancer.ConsistentHashLoadBalancer;
import com.joizhang.chat.gateway.loadbalancer.IPHashLoadBalancer;
import com.joizhang.chat.gateway.loadbalancer.HashLoadBalancerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.cloud.gateway.filter.headers.XForwardedHeadersFilter;
import org.springframework.cloud.gateway.support.DelegatingServiceInstance;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.*;

/**
 * A {@link GlobalFilter} implementation that routes requests using
 * {@link IPHashLoadBalancer} or {@link ConsistentHashLoadBalancer}.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Slf4j
public class ReactiveHashLoadBalancerClientFilter implements GlobalFilter, Ordered {

    public static final int LOAD_BALANCER_CLIENT_FILTER_ORDER = 10150;

    private static final String IP_HASH_SCHEMA = "hashlb";

    private final GatewayLoadBalancerProperties loadBalancerProperties;

    private final LoadBalancerClientFactory clientFactory;


    private ReactorLoadBalancer<ServiceInstance> loadBalancer;

    public ReactiveHashLoadBalancerClientFilter(GatewayConfigProperties gatewayConfigProperties,
                                                GatewayLoadBalancerProperties loadBalancerProperties,
                                                LoadBalancerClientFactory clientFactory) {
        this.loadBalancerProperties = loadBalancerProperties;
        this.clientFactory = clientFactory;
        // 设置负载均衡器
        String name = gatewayConfigProperties.getHashLbServiceName();
        ObjectProvider<ServiceInstanceListSupplier> provider =
                clientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class);
        String loadBalancerType = gatewayConfigProperties.getHashLBType();
        if (StrUtil.isNotEmpty(loadBalancerType)) {
            if (HashLoadBalancerType.CONSISTENT_HASH.getName().equals(loadBalancerType)) {
                this.loadBalancer = new ConsistentHashLoadBalancer(provider, name);
            } else {
                this.loadBalancer = new IPHashLoadBalancer(provider, name);
            }
        }

    }

    @Override
    public int getOrder() {
        return LOAD_BALANCER_CLIENT_FILTER_ORDER;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        URI url = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        String schemePrefix = exchange.getAttribute(GATEWAY_SCHEME_PREFIX_ATTR);
        if (url == null || (!IP_HASH_SCHEMA.equals(url.getScheme()) && !IP_HASH_SCHEMA.equals(schemePrefix))) {
            return chain.filter(exchange);
        }
        // preserve the original url
        addOriginalRequestUrl(exchange, url);

        if (log.isTraceEnabled()) {
            log.trace(ReactiveLoadBalancerClientFilter.class.getSimpleName() + " url before: " + url);
        }
        URI requestUri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        String serviceId = requestUri.getHost();
        Set<LoadBalancerLifecycle> supportedLifecycleProcessors =
                LoadBalancerLifecycleValidator.getSupportedLifecycleProcessors(
                        clientFactory.getInstances(serviceId, LoadBalancerLifecycle.class),
                        RequestDataContext.class,
                        ResponseData.class,
                        ServiceInstance.class
                );

        // set X-Forwarded-For
        ServerHttpRequest request = exchange.getRequest();
        String remoteAddr = Objects.requireNonNull(request.getRemoteAddress()).getAddress().getHostAddress();
        Map<String, Object> attributes = new HashMap<>();
        attributes.put(XForwardedHeadersFilter.X_FORWARDED_FOR_HEADER, remoteAddr);

        DefaultRequest<RequestDataContext> lbRequest = new DefaultRequest<>(
                new RequestDataContext(
                        new RequestData(request, attributes),
                        getHint(serviceId)
                )
        );
        LoadBalancerProperties loadBalancerProperties = clientFactory.getProperties(serviceId);
        return choose(lbRequest, serviceId, supportedLifecycleProcessors)
                .doOnNext(response -> {
                    if (!response.hasServer()) {
                        supportedLifecycleProcessors.forEach(lifecycle -> lifecycle
                                .onComplete(
                                        new CompletionContext<>(CompletionContext.Status.DISCARD, lbRequest, response)
                                )
                        );
                        throw NotFoundException.create(
                                this.loadBalancerProperties.isUse404(),
                                "Unable to find instance for " + url.getHost()
                        );
                    }

                    ServiceInstance retrievedInstance = response.getServer();

                    URI uri = request.getURI();

                    // if the `lb:<scheme>` mechanism was used, use `<scheme>` as the default,
                    // if the load balancer doesn't provide one.
                    String overrideScheme = retrievedInstance.isSecure() ? "https" : "http";
                    if (schemePrefix != null) {
                        overrideScheme = url.getScheme();
                    }

                    DelegatingServiceInstance serviceInstance =
                            new DelegatingServiceInstance(retrievedInstance, overrideScheme);

                    URI requestUrl = reconstructURI(serviceInstance, uri);

                    if (log.isTraceEnabled()) {
                        log.trace("LoadBalancerClientFilter url chosen: " + requestUrl);
                    }
                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, requestUrl);
                    exchange.getAttributes().put(GATEWAY_LOADBALANCER_RESPONSE_ATTR, response);
                    supportedLifecycleProcessors.forEach(lifecycle -> lifecycle.onStartRequest(lbRequest, response));
                })
                .then(chain.filter(exchange))
                .doOnError(throwable -> supportedLifecycleProcessors
                        .forEach(lifecycle ->
                                lifecycle.onComplete(
                                        new CompletionContext<ResponseData, ServiceInstance, RequestDataContext>(
                                                CompletionContext.Status.FAILED,
                                                throwable,
                                                lbRequest,
                                                exchange.getAttribute(GATEWAY_LOADBALANCER_RESPONSE_ATTR)
                                        )
                                )
                        )
                )
                .doOnSuccess(aVoid -> supportedLifecycleProcessors
                        .forEach(lifecycle ->
                                lifecycle.onComplete(
                                        new CompletionContext<ResponseData, ServiceInstance, RequestDataContext>(
                                                CompletionContext.Status.SUCCESS,
                                                lbRequest,
                                                exchange.getAttribute(GATEWAY_LOADBALANCER_RESPONSE_ATTR),
                                                buildResponseData(exchange, loadBalancerProperties.isUseRawStatusCodeInResponseData())
                                        )
                                )
                        )
                );
    }

    private ResponseData buildResponseData(ServerWebExchange exchange, boolean useRawStatusCodes) {
        if (useRawStatusCodes) {
            return new ResponseData(new RequestData(exchange.getRequest()), exchange.getResponse());
        }
        return new ResponseData(exchange.getResponse(), new RequestData(exchange.getRequest()));
    }

    protected URI reconstructURI(ServiceInstance serviceInstance, URI original) {
        return LoadBalancerUriTools.reconstructURI(serviceInstance, original);
    }

    private Mono<Response<ServiceInstance>> choose(Request<RequestDataContext> lbRequest,
                                                   String serviceId,
                                                   Set<LoadBalancerLifecycle> supportedLifecycleProcessors) {
        if (loadBalancer == null) {
            loadBalancer = this.clientFactory.getInstance(serviceId, ReactorServiceInstanceLoadBalancer.class);
        }
        if (loadBalancer == null) {
            throw new NotFoundException("No loadbalancer available for " + serviceId);
        }
        supportedLifecycleProcessors.forEach(lifecycle -> lifecycle.onStart(lbRequest));
        return loadBalancer.choose(lbRequest);
    }

    private String getHint(String serviceId) {
        LoadBalancerProperties loadBalancerProperties = clientFactory.getProperties(serviceId);
        Map<String, String> hints = loadBalancerProperties.getHint();
        String defaultHint = hints.getOrDefault("default", "default");
        String hintPropertyValue = hints.get(serviceId);
        return hintPropertyValue != null ? hintPropertyValue : defaultHint;
    }
}
