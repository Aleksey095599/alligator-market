package com.alligator.market.backend.provider.twelve.free;

import com.alligator.market.backend.config.http.ProviderHttpConfigGlobal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Проверяем, что TwelveFreeWebConfig «подтягивает» base-url из настроек
 * и прописывает его во WebClient.
 */
@TestPropertySource(properties = {
        "provider.connection-config.twelve-free.base-url=https://api.test/mock",
        "provider.connection-config.twelve-free.api-key=dummy"
})
@SpringBootTest(classes = {
        ProviderHttpConfigGlobal.class,
        TwelveFreeWebConfig.class,
        TwelveFreeProps.class
})
class TwelveFreeWebConfigTest {

    @Autowired
    @Qualifier("twelveFreeWebClient")
    WebClient client;

    @Test
    void baseUrlIsBound() {
        // --> «Подсматриваем» приватное поле baseUrl через ReflectionTestUtils
        String baseUrl = (String) ReflectionTestUtils.getField(client, "baseUrl");

        assertEquals("https://api.test/mock", baseUrl,
                "WebClient should contain baseUrl taken from application properties");
    }
}