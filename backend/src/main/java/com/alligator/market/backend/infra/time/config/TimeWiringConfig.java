package com.alligator.market.backend.infra.time.config;

import com.alligator.market.backend.infra.time.prop.AppTimeProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

/**
 * Wiring-конфигурация {@link AppTimeProperties}.
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(AppTimeProperties.class)
public class TimeWiringConfig {

    /* Технические часы приложения в UTC. */
    @Bean
    public Clock utcClock() {
        return Clock.systemUTC();
    }

    /* Бизнесовая временная зона приложения. */
    @Bean
    public ZoneId businessZoneId(AppTimeProperties properties) {
        return ZoneId.of(properties.businessZoneId());
    }
}
