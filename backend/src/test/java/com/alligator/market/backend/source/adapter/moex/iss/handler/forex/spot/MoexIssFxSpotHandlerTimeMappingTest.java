package com.alligator.market.backend.source.adapter.moex.iss.handler.forex.spot;

import com.alligator.market.backend.source.adapter.moex.iss.MoexIssMarketDataSource;
import com.alligator.market.backend.source.adapter.moex.iss.instrument.forex.spot.handler.MoexIssFxSpotHandler;
import com.alligator.market.backend.source.adapter.moex.iss.instrument.forex.spot.handler.MoexIssFxSpotHandlerPolicy;
import com.alligator.market.domain.instrument.asset.forex.fxspot.FxSpot;
import com.alligator.market.domain.instrument.asset.forex.fxspot.FxSpotTenor;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.Currency;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;
import com.alligator.market.domain.marketdata.tick.level.source.SourceTick;
import com.alligator.market.domain.marketdata.tick.level.source.type.SourceLastPriceTick;
import com.alligator.market.domain.marketdata.tick.level.source.vo.SourceInstrumentCode;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MoexIssFxSpotHandlerTimeMappingTest {

    @Test
    void mapsLastPriceTickTimeFromMarketdataTimeField() {
        WebClient webClient = WebClient.builder()
                .exchangeFunction(request -> Mono.just(ClientResponse.create(HttpStatus.OK)
                        .header("Content-Type", "application/json")
                        .body("""
                                {
                                  "marketdata": {
                                    "columns": ["SYSTIME", "TIME", "LAST"],
                                    "data": [["2026-05-22 14:26:53", "14:11:52", 10.4675]]
                                  }
                                }
                                """)
                        .build()))
                .build();

        MoexIssFxSpotHandler handler = new MoexIssFxSpotHandler(
                webClient,
                new MoexIssFxSpotHandlerPolicy(Duration.ofSeconds(5))
        );
        MoexIssMarketDataSource source = new MoexIssMarketDataSource(Set.of(handler));

        SourceTick tick = Flux.from(source.streamSourceTicks(cnyRubTom()))
                .take(1)
                .blockFirst(Duration.ofSeconds(1));

        SourceLastPriceTick lastPriceTick = (SourceLastPriceTick) tick;

        assertThat(lastPriceTick.sourceTickTime())
                .isEqualTo(Instant.parse("2026-05-22T11:11:52Z"));
    }

    @Test
    void supportsUsdRubSpotWithMoexSpotSecid() {
        WebClient webClient = WebClient.builder()
                .exchangeFunction(request -> Mono.just(ClientResponse.create(HttpStatus.OK)
                        .header("Content-Type", "application/json")
                        .body("""
                                {
                                  "marketdata": {
                                    "columns": ["SYSTIME", "TIME", "LAST"],
                                    "data": [["2026-05-22 14:26:53", "14:11:52", 80.2500]]
                                  }
                                }
                                """)
                        .build()))
                .build();

        MoexIssFxSpotHandler handler = new MoexIssFxSpotHandler(
                webClient,
                new MoexIssFxSpotHandlerPolicy(Duration.ofSeconds(5))
        );
        MoexIssMarketDataSource source = new MoexIssMarketDataSource(Set.of(handler));

        SourceTick tick = Flux.from(source.streamSourceTicks(usdRubSpot()))
                .take(1)
                .blockFirst(Duration.ofSeconds(1));

        SourceLastPriceTick lastPriceTick = (SourceLastPriceTick) tick;

        assertThat(lastPriceTick.sourceInstrumentCode()).isEqualTo(SourceInstrumentCode.of("USDRUB_SPT"));
    }

    private static FxSpot cnyRubTom() {
        Currency cny = new Currency(CurrencyCode.of("CNY"), "Chinese Yuan", "China", 2);
        Currency rub = new Currency(CurrencyCode.of("RUB"), "Russian Ruble", "Russian Federation", 2);
        return new FxSpot(cny, rub, FxSpotTenor.TOM, 4);
    }

    private static FxSpot usdRubSpot() {
        Currency usd = new Currency(CurrencyCode.of("USD"), "United States Dollar", "United States", 2);
        Currency rub = new Currency(CurrencyCode.of("RUB"), "Russian Ruble", "Russian Federation", 2);
        return new FxSpot(usd, rub, FxSpotTenor.SPOT, 4);
    }
}
