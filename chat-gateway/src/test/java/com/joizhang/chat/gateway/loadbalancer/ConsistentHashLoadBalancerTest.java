package com.joizhang.chat.gateway.loadbalancer;

import org.junit.jupiter.api.Test;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.support.SimpleObjectProvider;

import static org.junit.jupiter.api.Assertions.*;

class ConsistentHashLoadBalancerTest {

    private ConsistentHashLoadBalancer loadBalancer;

    @Test
    void shouldReturnEmptyResponseWhenSupplierNotAvailable() {
        loadBalancer = new ConsistentHashLoadBalancer(new SimpleObjectProvider<>(null), "test");
        Response<ServiceInstance> response = loadBalancer.choose().block();
        assertFalse(response.hasServer());
    }

}