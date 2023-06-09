package com.joizhang.chat.gateway.loadbalancer;

import cn.hutool.core.lang.ConsistentHash;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.gateway.filter.headers.XForwardedHeadersFilter;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.SelectedInstanceCallback;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class ConsistentHashLoadBalancer implements ReactorServiceInstanceHashLoadBalancer {

    final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    final String serviceId;

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
                .getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next()
                .map(serviceInstances -> processInstanceResponse(supplier, serviceInstances, request));
    }

    private Response<ServiceInstance> processInstanceResponse(ServiceInstanceListSupplier supplier,
                                                              List<ServiceInstance> serviceInstances,
                                                              Request request) {
        Response<ServiceInstance> serviceInstanceResponse = getInstanceResponse(serviceInstances, request);
        if (supplier instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
            ((SelectedInstanceCallback) supplier).selectedServiceInstance(serviceInstanceResponse.getServer());
        }
        return serviceInstanceResponse;
    }

    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances, Request request) {
        if (instances.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("No servers available for service: " + serviceId);
            }
            return new EmptyResponse();
        }

        // Do not move position when there is only 1 instance, especially some suppliers
        // have already filtered instances
        if (instances.size() == 1) {
            return new DefaultResponse(instances.get(0));
        }

        ConsistentHash<ServiceInstance> consistentHash = new ConsistentHash<>(2, instances);
        RequestDataContext requestContext = (RequestDataContext) request.getContext();
        Map<String, Object> attributes = requestContext.getClientRequest().getAttributes();
        String host = (String) attributes.get(XForwardedHeadersFilter.X_FORWARDED_FOR_HEADER);
        ServiceInstance instance = consistentHash.get(host);

        return new DefaultResponse(instance);
    }
}
