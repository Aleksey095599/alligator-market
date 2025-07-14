package com.alligator.market.backend.provider.twelve.free.adapter;

import com.alligator.market.backend.config.http.ProviderHttpConfigGlobal;
import com.alligator.market.backend.provider.twelve.free.config.TwelveFreeProps;
import com.alligator.market.backend.provider.twelve.free.config.web.TwelveFreeWebConfig;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {
        ProviderHttpConfigGlobal.class,
        TwelveFreeWebConfig.class,
        TwelveFreeAdapterV2.class
})
@EnableConfigurationProperties(TwelveFreeProps.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TwelveFreeAdapterSmokeTest {

    private MockWebServer server;

    // Один раз перед запуском всех тестов этого класса поднимаем виртуальный сервер
    @BeforeAll
    void start() throws Exception {
        server = new MockWebServer();
        server.start();
    }

    // После окончания тестов закрываем сервер
    @AfterAll
    void stop() throws Exception {
        server.close();
    }

    // Подменяем base-url и api-key только для этого тестового контекста
    @DynamicPropertySource
    static void overrideBaseUrl(DynamicPropertyRegistry reg) {
        reg.add("provider.connection-config.twelve-free.base-url",
                () -> server.url("/").toString());
        reg.add("provider.connection-config.twelve-free.api-key",
                () -> "2b8e2659372340d5b922cd6b8d6d2cb2");
    }

    @Autowired
    TwelveFreeAdapterV2 adapter;

    @Test
    void shouldHitCorrectEndpointAndParsePrice() throws Exception {

        // 1. Заглушка отдаёт цену 1.1087
        server.enqueue(new MockResponse()
                .setBody("{\"price\":\"1.1087\"}")
                .addHeader("Content-Type", "application/json"));

        // 2. Вызываем адаптер (Mono/Flux — неважно; здесь Mono для краткости)
        StepVerifier.create(adapter.streamQuotes("EUR/USD"))
                .assertNext(q -> {
                    assertEquals("EUR/USD", q.symbol());
                    assertEquals(new BigDecimal("1.1087"), q.price());
                })
                .verifyComplete();

        // 3. Проверяем, что реально ушли по нужному пути
        RecordedRequest rq = server.takeRequest();
        assertEquals("/price?symbol=EUR%2FUSD&apikey=2b8e2659372340d5b922cd6b8d6d2cb2",
                rq.getPath());
    }
}