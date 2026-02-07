package com.alligator.market.backend.provider.adapter.moex.iss.handler.forex.spot;

import com.alligator.market.backend.provider.adapter.moex.iss.MoexIssAdapter;
import com.alligator.market.backend.provider.adapter.moex.iss.handler.instrument.forex.spot.MoexIssFxSpotHandler;
import com.alligator.market.backend.provider.adapter.moex.iss.properties.MoexIssConnectionProperties;
import com.alligator.market.domain.instrument.type.forex.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.currency.vo.CurrencyCode;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpotTenor;
import com.alligator.market.domain.quote.tick.model.QuoteTick;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционный тест quote(...) для {@link MoexIssFxSpotHandler} с заглушкой MOEX ISS.
 */
@Disabled("Manual run only: long integration scenario")
class MoexIssFxSpotHandlerQuoteMockTest {

    private MockWebServer mockWebServer;
    private MoexIssFxSpotHandler handler;
    private FxSpot cnyRubTom;

    /* Формат и зона должны совпадать с обработчиком. */
    private static final DateTimeFormatter MOEX_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final ZoneId MOEX_ZONE = ZoneId.of("Europe/Moscow");

    @BeforeEach
    void setUp() throws IOException {
        // 1) Поднимаем мок‑сервер
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        // 2) Готовим baseUrl для WebClient и props
        String baseUrl = mockWebServer.url("/iss").toString();
        MoexIssConnectionProperties connectionProps = new MoexIssConnectionProperties(baseUrl);

        // 3) Создаём WebClient с тестовым baseUrl
        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();

        // 4) Создаём обработчик и прикрепляем к реальному адаптеру
        handler = new MoexIssFxSpotHandler(connectionProps, webClient);
        handler.attachTo(new MoexIssAdapter(connectionProps, webClient));

        // 5) Готовим инструмент CNYRUB_TOM
        Currency cny = new Currency(CurrencyCode.of("CNY"), "Chinese Yuan", "China", 2);
        Currency rub = new Currency(CurrencyCode.of("RUB"), "Russian Ruble", "Russian Federation", 2);
        cnyRubTom = new FxSpot(cny, rub, FxSpotTenor.TOM, 4);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void quoteParsesValidMarketdataIntoQuoteTick() throws Exception {
        // 1) Готовим валидный ответ мок‑сервера
        String systime = "2025-03-01 19:00:03";
        String json = """
                {
                  "marketdata": {
                    "columns": ["SYSTIME", "LAST"],
                    "data": [
                      ["%s", 10.95]
                    ]
                  }
                }
                """.formatted(systime);

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody(json));

        // 2) Запускаем цепочку: ожидаем ОДИН QuoteTick
        Mono<QuoteTick> result = Mono.from(handler.quote(cnyRubTom));

        StepVerifier.create(result)
                .assertNext(tick -> {
                    assertNotNull(tick, "QuoteTick must not be null");
                    assertEquals(cnyRubTom.instrumentCode(), tick.instrumentCode());
                    assertEquals("MOEX_ISS", tick.providerCode().value());

                    assertEquals(new BigDecimal("10.95"), tick.last());
                    assertNull(tick.bid());
                    assertNull(tick.ask());

                    assertNotNull(tick.exchangeTimestamp());
                    assertNotNull(tick.receivedTimestamp());

                    // Проверяем, что exchangeTimestamp совпадает с SYSTIME в зоне MOEX
                    LocalDateTime ldt = LocalDateTime.parse(systime, MOEX_DATETIME);
                    Instant expectedExchange = ldt.atZone(MOEX_ZONE).toInstant();
                    assertEquals(expectedExchange, tick.exchangeTimestamp());

                    assertFalse(tick.receivedTimestamp().isBefore(tick.exchangeTimestamp()),
                            "receivedTimestamp must not be before exchangeTimestamp");
                })
                .verifyComplete();

        // 3) Проверяем, что был сделан корректный HTTP‑запрос к мок‑серверу
        RecordedRequest recorded = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
        assertNotNull(recorded, "Expected MOEX ISS HTTP request but got null");
        assertEquals("GET", recorded.getMethod());

        HttpUrl url = recorded.getRequestUrl();
        assertNotNull(url, "Request URL must be parsed by MockWebServer");
        assertEquals("/iss/engines/currency/markets/selt/boards/CETS/securities/CNYRUB_TOM.json",
                url.encodedPath());
        assertEquals("off", url.queryParameter("iss.meta"));
        assertEquals("marketdata", url.queryParameter("iss.only"));
        assertEquals("SYSTIME,LAST", url.queryParameter("marketdata.columns"));
    }

    @Test
    void quoteFailsWhenMarketdataMissing() {
        String json = """
                {
                  "something_else": {}
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody(json));

        Mono<QuoteTick> result = Mono.from(handler.quote(cnyRubTom));

        StepVerifier.create(result)
                .expectErrorSatisfies(ex -> {
                    assertInstanceOf(IllegalStateException.class, ex);
                    assertTrue(ex.getMessage().contains("has no 'marketdata' object"));
                })
                .verify();
    }
}
