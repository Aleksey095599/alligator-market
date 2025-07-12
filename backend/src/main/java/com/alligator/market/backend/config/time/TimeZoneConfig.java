package com.alligator.market.backend.config.time;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

/**
 * Конфигурация временной зоны приложения.
 */
@Configuration
public class TimeZoneConfig {

    @Value("${app.time-zone:UTC}")
    private String timeZone;

    /**
     * Устанавливает временную зону приложения на основе значения из конфигурации.
     */
    @PostConstruct
    public void init() {
        TimeZone tz = TimeZone.getTimeZone(timeZone);
        TimeZone.setDefault(tz);
    }
}
