package com.alligator.market.backend.source.adapter.twelvedata.handler.forex.spot;

import com.alligator.market.backend.source.adapter.twelvedata.TwelveDataSource;
import com.alligator.market.backend.source.adapter.twelvedata.instrument.forex.spot.handler.TwelveDataFxSpotHandler;
import com.alligator.market.backend.source.adapter.twelvedata.instrument.forex.spot.handler.TwelveDataFxSpotHandlerPolicy;
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

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class TwelveDataFxSpotHandlerExchangeRateMappingTest {

    @Test
    void mapsExchangeRateResponseToLastPriceTick() {
        WebClient webClient = WebClient.builder()
                .exchangeFunction(request -> Mono.just(ClientResponse.create(HttpStatus.OK)
                        .header("Content-Type", "application/json")
                        .body("""
                                {
                                  "symbol": "INR/USD",
                                  "rate": "0.010450",
                                  "timestamp": 1700000000
                                }
                                """)
                        .build()))
                .build();

        TwelveDataFxSpotHandler handler = new TwelveDataFxSpotHandler(
                webClient,
                "test-api-key",
                new TwelveDataFxSpotHandlerPolicy(Duration.ofMinutes(5))
        );
        TwelveDataSource source = new TwelveDataSource(Set.of(handler));

        SourceTick tick = Flux.from(source.streamSourceTicks(inrUsdSpot()))
                .take(1)
                .blockFirst(Duration.ofSeconds(1));

        SourceLastPriceTick lastPriceTick = (SourceLastPriceTick) tick;

        assertThat(lastPriceTick.sourceInstrumentCode()).isEqualTo(SourceInstrumentCode.of("INRUSD"));
        assertThat(lastPriceTick.lastPrice()).isEqualByComparingTo(new BigDecimal("0.010450"));
        assertThat(lastPriceTick.sourceTickTime()).isEqualTo(Instant.ofEpochSecond(1700000000));
    }

    private static FxSpot inrUsdSpot() {
        Currency inr = new Currency(CurrencyCode.of("INR"), "Indian Rupee", "India", 2);
        Currency usd = new Currency(CurrencyCode.of("USD"), "United States Dollar", "United States", 2);
        return new FxSpot(inr, usd, FxSpotTenor.SPOT, 6);
    }
}
