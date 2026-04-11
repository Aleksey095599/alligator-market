package com.alligator.market.backend.provider.adapter.moex.iss.handler.forex.spot;

import com.alligator.market.backend.provider.adapter.moex.iss.MoexIssProvider;
import com.alligator.market.backend.provider.adapter.moex.iss.instrument.forex.spot.handler.MoexIssFxSpotHandler;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.model.Currency;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.model.vo.CurrencyCode;
import com.alligator.market.domain.instrument.asset.forex.spot.model.FxSpot;
import com.alligator.market.domain.instrument.asset.forex.spot.model.FxSpotTenor;
import com.alligator.market.domain.marketdata.tick.model.QuoteTick;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционный тест {@link MoexIssFxSpotHandler} с реальным запросом котировки.
 */
@Tag("dev")
class MoexIssFxSpotHandlerQuoteLiveTest {

    @Test
    @Tag("external")
    @Tag("slow")
    void liveQuoteForCnyRubTom() {
        // 1) Собираем WebClient с реальным baseUrl
        String baseUrl = "https://iss.moex.com/iss";

        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("User-Agent", "Alligator Market TEST")
                .build();

        // 2) Собираем реальный обработчик и провайдер
        MoexIssFxSpotHandler handler = new MoexIssFxSpotHandler(webClient);
        MoexIssProvider provider = new MoexIssProvider(Set.of(handler));

        // 3) Инструмент для теста
        Currency cny = new Currency(CurrencyCode.of("CNY"), "Chinese Yuan", "China", 2);
        Currency rub = new Currency(CurrencyCode.of("RUB"), "Russian Ruble", "Russian Federation", 2);
        FxSpot cnyRubTom = new FxSpot(cny, rub, FxSpotTenor.TOM, 4);

        // 4) Запускаем запрос к реальному MOEX ISS
        Mono<QuoteTick> result = Mono.from(provider.quote(cnyRubTom));

        // 5) Проверяем минимальные инварианты QuoteTick, не завязываясь на конкретную цену
        StepVerifier.create(result)
                .assertNext(tick -> {
                    // ВРЕМЕННЫЙ вывод для наглядности
                    System.out.println("=== LIVE QUOTETICK FROM MOEX ISS ===");
                    System.out.println(tick); // для record будет нормальный toString()
                    System.out.println("====================================");

                    assertNotNull(tick, "QuoteTick must not be null");
                    assertEquals(cnyRubTom.instrumentCode(), tick.instrumentCode(), "Instrument code must match");
                    assertEquals("MOEX_ISS", tick.providerCode().value(), "Provider code must be MOEX_ISS");

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
