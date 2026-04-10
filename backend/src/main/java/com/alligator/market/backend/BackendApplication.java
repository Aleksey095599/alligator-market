package com.alligator.market.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.ZoneOffset;
import java.util.TimeZone;

/**
 * Точка входа приложения.
 */
@SpringBootApplication
@EnableScheduling
@ConfigurationPropertiesScan("com.alligator.market.backend")
public class BackendApplication {

    private static final TimeZone TECHNICAL_TIME_ZONE = TimeZone.getTimeZone(ZoneOffset.UTC);

    public static void main(String[] args) {
        // Глобальная техническая зона JVM для всего приложения.
        TimeZone.setDefault(TECHNICAL_TIME_ZONE);

        SpringApplication.run(BackendApplication.class, args);
    }
}
