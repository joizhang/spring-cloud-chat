package com.joizhang.chat.gateway.loadbalancer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum HashLoadBalancerType {

    IP_HASH("ip_hash"),

    CONSISTENT_HASH("consistent_hash");

    private final String name;

}
