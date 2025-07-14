package com.alligator.market.backend.config.http;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тест проверяет, что бин "providerHttpClient" и бин "providerConnectionPool" успешно создаются
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