package com.alligator.market.backend.config.time;

import jakarta.annotation.PostConstruct;
import org.hibernate.cfg.AvailableSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.TimeZone;

/**
 * Конфигурация времени в приложении.
 *
 * <p>Настраивает временную зону для системы, Jackson и Hibernate.</p>
 */
@Configuration
public class TimeZoneConfig {

    private static final Logger log = LoggerFactory.getLogger(TimeZoneConfig.class);

    private static final ZoneId UTC = ZoneOffset.UTC;

    /**
     * Устанавливает дефолтную временную зону.
     */
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(UTC));
        log.info("Default time zone set to UTC");
    }

    /**
     * Настраивает временную зону для сериализации JSON.
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonTimeZoneCustomizer() {
        return builder -> builder.timeZone(TimeZone.getTimeZone(UTC));
    }

    /**
     * Настраивает временную зону для Hibernate.
     */
    @Bean
    public HibernatePropertiesCustomizer hibernateTimeZoneCustomizer() {
        return properties -> properties.put(AvailableSettings.JDBC_TIME_ZONE, UTC);
    }
}
