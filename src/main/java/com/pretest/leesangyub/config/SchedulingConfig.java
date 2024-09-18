package com.pretest.leesangyub.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableScheduling // @EnableScheduling 을 해야 스케쥴링 기능 활성화됨.
public class SchedulingConfig {


}
