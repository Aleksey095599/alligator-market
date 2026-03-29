package com.alligator.market.backend.infra.time.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.TimeZone;

/**
 * Конфигурационный класс для времени в приложении.
 *
 * <p>Настраивает временную зону для системы, Jackson и Hibernate.</p>
 */
@Configuration(proxyBeanMethods = false)
@Slf4j
public class TimeZoneConfig {

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
