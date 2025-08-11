package com.alligator.market.backend.config.time;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;
import java.util.TimeZone;

/**
 * Конфигурация временной зоны приложения.
 */
@Configuration
public class TimeZoneConfig {

    @Value("${app.time-zone:UTC}")
    private String timeZone;

    /**
     * Устанавливает временную зону приложения.
     *
     * @throws IllegalArgumentException
     */
    @PostConstruct
    public void init() {

        // Идентификатор временной зоны
        final ZoneId zoneId;

        try {
            //
            zoneId = ZoneId.of(timeZone);
        } catch (Exception ex) {
            // Явно валим старт, чтобы не было «тихого» перехода в GMT
            throw new IllegalArgumentException("Invalid time zone id: " + timeZone, ex);
        }

        // Устанавливаем дефолтную зону для JVM
        TimeZone.setDefault(TimeZone.getTimeZone(zoneId));
    }
}
