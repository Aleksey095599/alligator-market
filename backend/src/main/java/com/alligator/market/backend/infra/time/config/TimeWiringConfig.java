package com.alligator.market.backend.infra.time.config;

import com.alligator.market.backend.infra.time.prop.AppTimeProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(AppTimeProperties.class)
public class TimeWiringConfig {
    @Bean
    public Clock utcClock() {
        return Clock.systemUTC();
    }

    @Bean
    public ZoneId businessZoneId(AppTimeProperties properties) {
        return ZoneId.of(properties.businessZoneId());
    }
}
