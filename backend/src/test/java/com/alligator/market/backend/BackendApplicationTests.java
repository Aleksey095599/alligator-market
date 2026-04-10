package com.alligator.market.backend;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Smoke-тест: проверяет, что Spring context приложения поднимается в test-профиле.
 */
@Tag("smoke")
@SpringBootTest
@ActiveProfiles("test")
class BackendApplicationTests {

    /* Проверка проходит, если контекст приложения стартует без ошибок. */
    @Test
    void contextLoads() {
    }

}
