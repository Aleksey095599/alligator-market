package com.alligator.market.backend.provider.adapter.moex.iss.handler.forex.spot;

import com.alligator.market.backend.provider.adapter.moex.iss.MoexIssAdapter;
import com.alligator.market.backend.provider.adapter.moex.iss.config.MoexIssAdapterProps;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.CurrencyCode;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpotValueDate;
import com.alligator.market.domain.quote.tick.model.QuoteTick;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционный тест {@link MoexIssFxSpotHandler} с реальным запросом котировки.
 */
@Disabled("Manual run only: long integration scenario")
class MoexIssFxSpotHandlerQuoteLiveTest {

    @Test
    @Tag("external")
    @Tag("slow")
    void liveQuoteForCnyRubTom() {
        // 1) Собираем WebClient с реальным baseUrl и задаем настройки подключения
        String baseUrl = "https://iss.moex.com/iss";
        MoexIssAdapterProps props = new MoexIssAdapterProps(baseUrl);

        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("User-Agent", "Alligator Market TEST")
                .build();

        // 2) Собираем реальный обработчик и адаптер
        MoexIssFxSpotHandler handler = new MoexIssFxSpotHandler(props, webClient);
        handler.attachTo(new MoexIssAdapter(props, webClient));

        // 3) Доменный инструмент для теста
        Currency cny = new Currency(CurrencyCode.of("CNY"), "Chinese Yuan", "China", 2);
        Currency rub = new Currency(CurrencyCode.of("RUB"), "Russian Ruble", "Russian Federation", 2);
        FxSpot cnyRubTom = new FxSpot(cny, rub, FxSpotValueDate.TOM, 4);

        // 4) Запускаем запрос к реальному MOEX ISS
        Mono<QuoteTick> result = Mono.from(handler.quote(cnyRubTom));

        // Проверяем минимальные инварианты QuoteTick, не завязываясь на конкретную цену
        StepVerifier.create(result)
                .assertNext(tick -> {
                    // ВРЕМЕННЫЙ вывод для наглядности
                    System.out.println("=== LIVE QUOTETICK FROM MOEX ISS ===");
                    System.out.println(tick); // для record будет нормальный toString()
                    System.out.println("====================================");

                    assertNotNull(tick, "QuoteTick must not be null");
                    assertEquals(cnyRubTom.instrumentCode(), tick.instrumentCode(), "Instrument code must match");
                    assertEquals("MOEX_ISS", tick.providerCode(), "Provider code must be MOEX_ISS");

                    assertNotNull(tick.last(), "LAST price must not be null");
                    assertTrue(tick.last().compareTo(BigDecimal.ZERO) > 0, "LAST price must be positive");

                    assertNotNull(tick.exchangeTimestamp(), "Exchange timestamp must not be null");
                    assertNotNull(tick.receivedTimestamp(), "Received timestamp must not be null");
                    assertFalse(tick.receivedTimestamp().isBefore(tick.exchangeTimestamp()),
                            "receivedTimestamp must not be before exchangeTimestamp");
                })
                .verifyComplete();
    }
}
