package com.alligator.market.backend.source.adapter.moex.iss.handler.forex.spot;

import com.alligator.market.backend.source.adapter.moex.iss.MoexIssSource;
import com.alligator.market.backend.source.adapter.moex.iss.instrument.forex.spot.handler.MoexIssFxSpotHandler;
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
 * РРЅС‚РµРіСЂР°С†РёРѕРЅРЅС‹Р№ С‚РµСЃС‚ {@link MoexIssFxSpotHandler} СЃ СЂРµР°Р»СЊРЅС‹Рј Р·Р°РїСЂРѕСЃРѕРј source tick.
 */
@Tag("dev")
class MoexIssFxSpotHandlerStreamSourceTicksLiveTest {

    @Test
    @Tag("external")
    @Tag("slow")
    void liveSourceTicksForCnyRubTom() {
        // 1) РЎРѕР±РёСЂР°РµРј WebClient СЃ СЂРµР°Р»СЊРЅС‹Рј baseUrl
        String baseUrl = "https://iss.moex.com/iss";

        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("User-Agent", "Alligator Market TEST")
                .build();

        // 2) Build the real handler and source.
        MoexIssFxSpotHandler handler = new MoexIssFxSpotHandler(webClient);
        MoexIssSource source = new MoexIssSource(Set.of(handler));

        // 3) РРЅСЃС‚СЂСѓРјРµРЅС‚ РґР»СЏ С‚РµСЃС‚Р°
        Currency cny = new Currency(CurrencyCode.of("CNY"), "Chinese Yuan", "China", 2);
        Currency rub = new Currency(CurrencyCode.of("RUB"), "Russian Ruble", "Russian Federation", 2);
        FxSpot cnyRubTom = new FxSpot(cny, rub, FxSpotTenor.TOM, 4);

        // 4) Р—Р°РїСѓСЃРєР°РµРј Р·Р°РїСЂРѕСЃ Рє СЂРµР°Р»СЊРЅРѕРјСѓ MOEX ISS
        Mono<SourceMarketDataTick> result = Mono.from(source.streamSourceTicks(cnyRubTom));

        // 5) РџСЂРѕРІРµСЂСЏРµРј РјРёРЅРёРјР°Р»СЊРЅС‹Рµ РёРЅРІР°СЂРёР°РЅС‚С‹ source-level С‚РёРєР°, РЅРµ Р·Р°РІСЏР·С‹РІР°СЏСЃСЊ РЅР° РєРѕРЅРєСЂРµС‚РЅСѓСЋ С†РµРЅСѓ
        StepVerifier.create(result)
                .assertNext(tick -> {
                    // Р’Р Р•РњР•РќРќР«Р™ РІС‹РІРѕРґ РґР»СЏ РЅР°РіР»СЏРґРЅРѕСЃС‚Рё
                    System.out.println("=== LIVE SOURCE TICK FROM MOEX ISS ===");
                    System.out.println(tick); // РґР»СЏ record Р±СѓРґРµС‚ РЅРѕСЂРјР°Р»СЊРЅС‹Р№ toString()
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
