package com.pretest.leesangyub.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "taskapp.dbs")
public record DBsProperties (
        DBProperties pridb
        // DBProperties subdb
) {

    public record DBProperties(
            String driver,
            String ip,
            String port,
            String database,
            String user,
            String pass
    ) {
    }
}
