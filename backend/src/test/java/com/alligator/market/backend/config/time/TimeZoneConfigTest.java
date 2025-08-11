package com.alligator.market.backend.config.time;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.ZoneId;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тест конфигурации временной зоны.
 */
@SpringBootTest
@TestPropertySource(properties = "app.time-zone=UTC")
class TimeZoneConfigTest {

    @Autowired
    private AppTimeProps props;

    // Проверяем, что дефолтная зона устанавливается из настроек
    @Test
    void setsDefaultZone() {
        assertEquals(ZoneId.of("UTC"), props.timeZone());
        assertEquals(ZoneId.of("UTC"), TimeZone.getDefault().toZoneId());
    }
}
