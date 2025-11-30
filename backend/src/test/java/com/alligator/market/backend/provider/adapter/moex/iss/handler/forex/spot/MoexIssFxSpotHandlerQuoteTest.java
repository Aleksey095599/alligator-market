package com.alligator.market.backend.provider.adapter.moex.iss.handler.forex.spot;

import com.alligator.market.backend.provider.adapter.moex.iss.MoexIssAdapter;
import com.alligator.market.backend.provider.adapter.moex.iss.config.MoexIssAdapterProps;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.CurrencyCode;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpotValueDate;
import com.alligator.market.domain.quote.QuoteTick;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционный тест quote(...) для {@link MoexIssFxSpotHandler} на заглушке MOEX ISS.
 */
class MoexIssFxSpotHandlerQuoteTest {

    /* HTTP мок‑сервер, имитирующий MOEX ISS. */
    private MockWebServer mockWebServer;

    /* Тестируемый обработчик. */
    private MoexIssFxSpotHandler handler;

    /* Доменный инструмент CNY/RUB_TOM. */
    private FxSpot cnyRubTom;

    @BeforeEach
    void setUp() throws IOException {
        // 1) Поднимаем мок‑сервер
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        // 2) Готовим baseUrl для WebClient и props
        String baseUrl = mockWebServer.url("/iss").toString();
        MoexIssAdapterProps props = new MoexIssAdapterProps(baseUrl);

        // 3) Создаём WebClient с тестовым baseUrl
        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();

        // 4) Создаём хендлер и прикрепляем к реальному адаптеру (Mockito не подходит для sealed-интерфейса)
        handler = new MoexIssFxSpotHandler(props, webClient);
        handler.attachTo(new MoexIssAdapter(props, webClient));

        // 5) Готовим доменный инструмент CNY/RUB_TOM
        Currency cny = new Currency(CurrencyCode.of("CNY"), "Chinese Yuan", "China", 2);
        Currency rub = new Currency(CurrencyCode.of("RUB"), "Russian Ruble", "Russian Federation", 2);
        cnyRubTom = new FxSpot(cny, rub, FxSpotValueDate.TOM, 4);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void quoteRequestsMarketDataForCnyRubTom() throws Exception {
        // 1) Готовим ответ мок‑сервера с минимальным marketdata JSON
        String rawJson = "{\"marketdata\":{\"columns\":[\"LAST\",\"UPDATETIME\"],\"data\":[[8.888,\"2024-05-05 12:34:56\"]]}}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody(rawJson));

        // 2) Запускаем цепочку: ожидаем успешное завершение пустого Mono
        Mono<QuoteTick> result = Mono.from(handler.quote(cnyRubTom));
        StepVerifier.create(result).verifyComplete();

        // 3) Проверяем, что был сделан корректный HTTP-запрос к мок‑серверу
        RecordedRequest recorded = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
        assertNotNull(recorded, "Expected MOEX ISS HTTP request but got null");
        assertEquals("GET", recorded.getMethod());

        HttpUrl url = recorded.getRequestUrl();
        assertNotNull(url, "Request URL must be parsed by MockWebServer");
        assertEquals("/iss/engines/currency/markets/selt/securities/CNYRUB_TOM.json", url.encodedPath());
        assertEquals("off", url.queryParameter("iss.meta"));
        assertEquals("marketdata", url.queryParameter("iss.only"));
        assertEquals("LAST,UPDATETIME", url.queryParameter("marketdata.columns"));
    }
}
