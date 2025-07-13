package com.alligator.market.backend.config.http;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тест проверяет корректность создания и настройки бинов для HTTP-соединений.
 * Проверяется, что бины ConnectionProvider и HttpClient успешно создаются
 * и внедряются в контекст Spring.
 */
@SpringBootTest(classes = ProviderHttpConfigGlobal.class)
class ProviderHttpConfigGlobalTest {

    @Autowired
    @Qualifier("providerConnectionPool")
    ConnectionProvider pool;

    @Autowired
    @Qualifier("providerHttpClient")
    HttpClient httpClient;

    @Test
    void beansCreated() {

        // Проверяем что pool и httpClient ненулевые
        assertNotNull(pool);
        assertNotNull(httpClient);
    }
}