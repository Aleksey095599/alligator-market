package com.alligator.market.backend.config.time;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

/**
 * Конфигурация времени в приложении.
 */
@Configuration
public class TimeZoneConfig {

    private static final Logger log = LoggerFactory.getLogger(TimeZoneConfig.class);

    private final AppTimeProps props;

    // Конструктор
    public TimeZoneConfig(AppTimeProps props) {
        this.props = props;
    }

    /** Устанавливает дефолтную временную зону. */
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(props.timeZone()));
        log.info("Default time zone set to {}", props.timeZone());
    }
}
