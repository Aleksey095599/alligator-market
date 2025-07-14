package com.alligator.market.backend.provider.twelve.free.config.web;

import com.alligator.market.backend.config.http.ProviderHttpConfigGlobal;
import com.alligator.market.backend.provider.twelve.free.config.TwelveFreeConnectionProps;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Проверяем, что бин "twelveFreeWebClient" успешно создается и внедряется в контекст Spring и использует
 * корректный base-url.
 */
@TestPropertySource(properties = {
        "provider.connection-config.twelve-free.base-url=https://api.test/mock",
        "provider.connection-config.twelve-free.api-key=dummy"
})
@SpringBootTest(classes = {
        ProviderHttpConfigGlobal.class,
        TwelveFreeWebConfig.class
})
@EnableConfigurationProperties(TwelveFreeConnectionProps.class)
class TwelveFreeWebConfigTest {

    @Autowired
    @Qualifier("twelveFreeWebClient")
    WebClient client;

    @Test
    void beanCreated() {
        // Проверяем что client ненулевой
        assertNotNull(client);
    }

    @Test
    void baseUrlIsBound() {
        // «Подсматриваем» приватное поле baseUrl через ReflectionTestUtils
        String baseUrl = (String) ReflectionTestUtils.getField(client, "baseUrl");
        assertEquals("https://api.test/mock", baseUrl,
                "WebClient should contain baseUrl taken from application properties");
    }
}