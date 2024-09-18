package com.pretest.leesangyub.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "taskapp.redis")
public record RedisProperties(
        String host,
        int port,
        String pass
) {
}
