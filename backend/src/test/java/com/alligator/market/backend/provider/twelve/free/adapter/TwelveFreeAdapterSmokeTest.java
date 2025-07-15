package com.alligator.market.backend.provider.twelve.free.adapter;

import com.alligator.market.backend.config.http.ProviderHttpConfigGlobal;
import com.alligator.market.backend.provider.twelve.free.config.TwelveFreeProps;
import com.alligator.market.backend.provider.twelve.free.config.web.TwelveFreeWebConfig;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.math.BigDecimal;

import com.alligator.market.domain.instrument.Instrument;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {
        ProviderHttpConfigGlobal.class,
        TwelveFreeWebConfig.class,
        TwelveFreeAdapterV2.class
})
@EnableConfigurationProperties(TwelveFreeProps.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TwelveFreeAdapterSmokeTest {

    private static MockWebServer server;

    /* Закрываем заглушку после выполнения всех тестов */
    @AfterAll
    static void stop() {
        server.close();
    }

    /*
     * Запускаем MockWebServer и подменяем свойства подключения.
     * Таким образом, адаптер будет общаться не с реальным API,
     * а с локальной заглушкой.
     */
    @DynamicPropertySource
    static void overrideBaseUrl(DynamicPropertyRegistry reg) throws IOException {
        server = new MockWebServer();
        server.start();
        reg.add("provider.connection-config.twelve-free.base-url",
                () -> server.url("/").toString()); // Пример - http://localhost:51423/
        reg.add("provider.connection-config.twelve-free.api-key",
                () -> "2b8e2659372340d5b922cd6b8d6d2cb2");
    }

    @Autowired
    TwelveFreeAdapterV2 adapter;

    /*
     * Основной smoke-тест.
     * Проверяем, что адаптер формирует корректный запрос
     * и правильно преобразует полученный ответ в объект QuoteTick.
     */
    @Test
    void shouldHitCorrectEndpointAndParsePrice() throws IOException, InterruptedException {

        // Формируем ответ виртуального сервера-заглушки
        server.enqueue(new MockResponse()
                .setBody("{\"price\":\"1.1688\"}")
                .setHeader("Content-Type", "application/json"));

        // Проверка пути запроса котировки
        RecordedRequest rq = server.takeRequest();
        String actualPath = rq.getRequestUrl().encodedPath() + "?" + rq.getRequestUrl().encodedQuery();
        assertEquals("/price?symbol=EUR%2FUSD&apikey=2b8e2659372340d5b922cd6b8d6d2cb2",
                actualPath);

        // Запрашиваем котировку через адаптер и проверяем корректность полученного объекта QuoteTick
        StepVerifier.create(adapter.streamQuotes(new Instrument("EUR/USD")))
                .assertNext(q -> {
                    assertEquals("EUR/USD", q.symbol());
                    assertEquals(new BigDecimal("1.1087"), q.bid());
                    assertEquals(new BigDecimal("1.1087"), q.ask());
                    assertEquals("TWELVE_FREE_PLAN", q.provider());
                })
                .verifyComplete();
    }
}