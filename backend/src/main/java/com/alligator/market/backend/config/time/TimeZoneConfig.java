package com.alligator.market.backend.config.time;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;
import java.util.TimeZone;

/**
 * Конфигурация времени в приложении.
 */
@Configuration
public class TimeZoneConfig {

    private final AppTimeProps props;

    // Конструктор
    public TimeZoneConfig(AppTimeProps props) {
        this.props = props;
    }

    /** Устанавливает дефолтную временную зону. */
    @PostConstruct
    public void init() {
        ZoneId zoneId = ZoneId.of(props.timeZone());
        TimeZone.setDefault(TimeZone.getTimeZone(zoneId));
    }
}
