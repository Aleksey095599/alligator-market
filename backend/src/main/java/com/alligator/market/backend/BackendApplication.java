package com.alligator.market.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.ZoneOffset;
import java.util.TimeZone;

/* Точка входа приложения. */
@SpringBootApplication
@ComponentScan("com.alligator.market")
@EnableScheduling
@ConfigurationPropertiesScan("com.alligator.market")
public class BackendApplication {

    public static void main(String[] args) {
        // Глобальная техническая зона приложения.
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC));

        SpringApplication.run(BackendApplication.class, args);
    }
}
