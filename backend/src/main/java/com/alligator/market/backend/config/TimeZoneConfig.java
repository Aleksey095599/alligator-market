package com.alligator.market.backend.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

/**
 * Конфигурация временной зоны приложения.
 * Используем локальное время сервера для всего приложения.
 */
@Configuration
public class TimeZoneConfig {

    @Value("${app.time-zone:UTC}")
    private String timeZone;

    @PostConstruct
    public void init() {
        TimeZone tz = TimeZone.getTimeZone(timeZone);
        TimeZone.setDefault(tz);
    }
}
