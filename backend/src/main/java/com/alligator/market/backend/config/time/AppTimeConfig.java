package com.alligator.market.backend.config.time;

import com.alligator.market.backend.time.AppTimeProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

/**
 * Конфигурация времени приложения: бизнес-зона и единый источник "now".
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(AppTimeProperties.class)
public class AppTimeConfig {

    public static final String BEAN_APP_BUSINESS_ZONE_ID = "appBusinessZoneId";
    public static final String BEAN_APP_CLOCK = "appClock";

    /**
     * Бизнес-временная зона приложения.
     */
    @Bean(BEAN_APP_BUSINESS_ZONE_ID)
    public ZoneId appBusinessZoneId(AppTimeProperties props) {
        return ZoneId.of(props.businessZoneId());
    }

    /**
     * Часы приложения (используются вместо Instant.now()).
     */
    @Bean(BEAN_APP_CLOCK)
    public Clock appClock(ZoneId appBusinessZoneId) {
        return Clock.system(appBusinessZoneId);
    }
}
