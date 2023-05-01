package com.joizhang.chat.gateway.loadbalancer;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;

/**
 * A marker interface for Hash based {@link ReactorLoadBalancer} that allows selecting
 * {@link ServiceInstance} objects.
 *
 * @author joizhang
 */
public interface ReactorServiceInstanceHashLoadBalancer extends ReactorLoadBalancer<ServiceInstance> {
}
