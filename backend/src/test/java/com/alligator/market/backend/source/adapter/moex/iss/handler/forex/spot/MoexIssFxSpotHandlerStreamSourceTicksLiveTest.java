package com.alligator.market.backend.source.adapter.moex.iss.handler.forex.spot;

import com.alligator.market.backend.source.adapter.moex.iss.MoexIssSource;
import com.alligator.market.backend.source.adapter.moex.iss.instrument.forex.spot.handler.MoexIssFxSpotHandler;
import com.alligator.market.backend.source.adapter.moex.iss.instrument.forex.spot.handler.MoexIssFxSpotHandlerPolicy;
import com.alligator.market.domain.instrument.asset.forex.fxspot.FxSpot;
import com.alligator.market.domain.instrument.asset.forex.fxspot.FxSpotTenor;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.Currency;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;
import com.alligator.market.domain.marketdata.tick.level.source.SourceTick;
import com.alligator.market.domain.marketdata.tick.level.source.type.SourceLastPriceTick;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Live integration test for {@link MoexIssFxSpotHandler} against MOEX ISS.
 */
@Tag("dev")
class MoexIssFxSpotHandlerStreamSourceTicksLiveTest {

    @Test
    @Tag("external")
    @Tag("slow")
    void liveSourceTicksForCnyRubTomByPolling() {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://iss.moex.com/iss")
                .defaultHeader("User-Agent", "Alligator Market TEST")
                .build();

        MoexIssFxSpotHandler handler = new MoexIssFxSpotHandler(
                webClient,
                new MoexIssFxSpotHandlerPolicy(Duration.ofSeconds(5))
        );
        MoexIssSource source = new MoexIssSource(Set.of(handler));

        Currency cny = new Currency(CurrencyCode.of("CNY"), "Chinese Yuan", "China", 2);
        Currency rub = new Currency(CurrencyCode.of("RUB"), "Russian Ruble", "Russian Federation", 2);
        FxSpot cnyRubTom = new FxSpot(cny, rub, FxSpotTenor.TOM, 4);

        Flux<SourceTick> result = Flux.from(source.streamSourceTicks(cnyRubTom))
                .take(3)
                .timeout(Duration.ofSeconds(20));

        StepVerifier.create(result)
                .recordWith(ArrayList::new)
                .expectNextCount(3)
                .consumeRecordedWith(ticks -> ticks.forEach(
                        MoexIssFxSpotHandlerStreamSourceTicksLiveTest::assertCnyRubTomTick
                ))
                .verifyComplete();
    }

    private static void assertCnyRubTomTick(SourceTick tick) {
        System.out.println("LIVE SOURCE TICK FROM MOEX ISS: " + tick);

        assertNotNull(tick, "SourceMarketDataTick must not be null");
        SourceLastPriceTick lastPriceTick = assertInstanceOf(SourceLastPriceTick.class, tick);

        assertEquals(
                "CNYRUB_TOM",
                lastPriceTick.sourceInstrumentCode().value(),
                "Source instrument code must match"
        );

        assertNotNull(lastPriceTick.lastPrice(), "LAST price must not be null");
        assertTrue(lastPriceTick.lastPrice().compareTo(BigDecimal.ZERO) > 0, "LAST price must be positive");

        assertNotNull(lastPriceTick.sourceTickTime(), "Source tick time must not be null");
    }
}
