package com.pretest.leesangyub.config;

import com.pretest.leesangyub.PretaskApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationPropertiesScan(basePackageClasses = PretaskApplication.class)
public class PropertiesConfig {
    // basePackageClasses 위치 부터 하위 디렉토리까지 @ConfigurationProperties 가 달린 객체들을 찾아서 yml값 매핑 후 빈으로 등록해주는 클래스
}
