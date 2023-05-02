package com.joizhang.chat.gateway.loadbalancer;

import org.junit.jupiter.api.Test;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultRequest;
import org.springframework.cloud.client.loadbalancer.RequestData;
import org.springframework.cloud.client.loadbalancer.RequestDataContext;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.cloud.gateway.filter.headers.XForwardedHeadersFilter;
import org.springframework.cloud.loadbalancer.core.DiscoveryClientServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.SimpleObjectProvider;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IPHashLoadBalancerTest {

    private final ServiceInstance serviceInstance = new DefaultServiceInstance();

    private IPHashLoadBalancer loadBalancer;

    @Test
    void shouldReturnEmptyResponseWhenSupplierNotAvailable() {
        loadBalancer = new IPHashLoadBalancer(new SimpleObjectProvider<>(null), "test");
        Response<ServiceInstance> response = loadBalancer.choose().block();
        assertFalse(response.hasServer());
    }

    @Test
    void shouldReturnOneServiceInstance() {
        DiscoveryClientServiceInstanceListSupplier supplier = mock(DiscoveryClientServiceInstanceListSupplier.class);
        when(supplier.get(any())).thenReturn(Flux.just(Arrays.asList(serviceInstance, new DefaultServiceInstance())));
        loadBalancer = new IPHashLoadBalancer(new SimpleObjectProvider<>(supplier), "test");

        DefaultRequest<RequestDataContext> lbRequest = mock(DefaultRequest.class);
        RequestDataContext requestDataContext = mock(RequestDataContext.class);
        RequestData requestData = mock(RequestData.class);
        Map<String, Object> attributes = mock(Map.class);
        when(requestDataContext.getClientRequest()).thenReturn(requestData);
        when(requestData.getAttributes()).thenReturn(attributes);
        when(attributes.get(XForwardedHeadersFilter.X_FORWARDED_FOR_HEADER)).thenReturn("ahost");
        when(lbRequest.getContext()).thenReturn(requestDataContext);

        Response<ServiceInstance> responseA = loadBalancer.choose(lbRequest).block();
        assertTrue(responseA.hasServer());

        when(attributes.get(XForwardedHeadersFilter.X_FORWARDED_FOR_HEADER)).thenReturn("bhost");
        Response<ServiceInstance> responseB = loadBalancer.choose(lbRequest).block();
        assertTrue(responseB.hasServer());

        assertNotSame(responseA, responseB);
    }
}