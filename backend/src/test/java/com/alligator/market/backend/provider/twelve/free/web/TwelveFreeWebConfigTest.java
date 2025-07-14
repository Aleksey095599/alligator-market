package com.alligator.market.backend.provider.twelve.free.web;

import com.alligator.market.backend.config.http.ProviderHttpConfigGlobal;
import com.alligator.market.backend.provider.twelve.free.TwelveFreeProps;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Properties;

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
@EnableConfigurationProperties(TwelveFreeProps.class)
class TwelveFreeWebConfigTest {

    /*
     * Мини-конфигурация, подменяющая отсутствующий в тестовом контексте бин
     * BuildProperties (Spring Boot создаёт его только при наличии build-info).
     */
    @TestConfiguration
    static class BuildPropsStub {
        @Bean
        BuildProperties buildProperties() {
            // Двух полей «name» и «version» достаточно для любых toString() / header-log’ов
            Properties p = new Properties();
            p.put("name", "alligator-test");
            p.put("version", "0.0.0-SNAPSHOT");
            return new BuildProperties(p);
        }
    }

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