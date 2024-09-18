package com.pretest.leesangyub.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "taskapp.freelancer")
public record FreelancerProperties(
        String tableName,
        long scheduleInterval
) {

}
