package com.alligator.market.backend.provider.adapter.moex.iss.handler.forex.spot;

import com.alligator.market.backend.provider.adapter.moex.iss.MoexIssProvider;
import com.alligator.market.backend.provider.adapter.moex.iss.instrument.forex.spot.handler.MoexIssFxSpotHandler;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.Currency;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;
import com.alligator.market.domain.instrument.asset.forex.fxspot.FxSpot;
import com.alligator.market.domain.instrument.asset.forex.fxspot.classification.FxSpotTenor;
import com.alligator.market.domain.marketdata.tick.level.source.SourceMarketDataTick;
import com.alligator.market.domain.marketdata.tick.level.source.type.SourceLastPriceTick;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционный тест {@link MoexIssFxSpotHandler} с реальным запросом source tick.
 */
@Tag("dev")
class MoexIssFxSpotHandlerStreamSourceTicksLiveTest {

    @Test
    @Tag("external")
    @Tag("slow")
    void liveSourceTicksForCnyRubTom() {
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
        Mono<SourceMarketDataTick> result = Mono.from(provider.streamSourceTicks(cnyRubTom));

        // 5) Проверяем минимальные инварианты source-level тика, не завязываясь на конкретную цену
        StepVerifier.create(result)
                .assertNext(tick -> {
                    // ВРЕМЕННЫЙ вывод для наглядности
                    System.out.println("=== LIVE SOURCE TICK FROM MOEX ISS ===");
                    System.out.println(tick); // для record будет нормальный toString()
                    System.out.println("======================================");

                    assertNotNull(tick, "SourceMarketDataTick must not be null");
                    SourceLastPriceTick lastPriceTick = assertInstanceOf(SourceLastPriceTick.class, tick);

                    assertEquals(
                            "CNYRUB_TOM",
                            lastPriceTick.sourceInstrumentCode().value(),
                            "Source instrument code must match"
                    );

                    assertNotNull(lastPriceTick.lastPrice(), "LAST price must not be null");
                    assertTrue(lastPriceTick.lastPrice().compareTo(BigDecimal.ZERO) > 0, "LAST price must be positive");

                    assertNotNull(lastPriceTick.sourceTimestamp(), "Source timestamp must not be null");
                })
                .verifyComplete();
    }
}
