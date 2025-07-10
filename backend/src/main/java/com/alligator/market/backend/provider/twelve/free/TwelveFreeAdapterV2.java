package com.alligator.market.backend.provider.twelve.free;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.provider.AccessMethod;
import com.alligator.market.domain.provider.DeliveryMode;
import com.alligator.market.domain.provider.MarketDataProvider;
import com.alligator.market.domain.quote.QuoteTick;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Реализация адаптера провайдера TwelveData (free plan).
 */
@Component
public class TwelveFreeAdapterV2 implements MarketDataProvider {

    @Override
    public String providerCode() {
        return "TWELVE_FREE_PLAN";
    }

    @Override
    public DeliveryMode deliveryMode() {
        return DeliveryMode.PULL;
    }

    @Override
    public AccessMethod accessMethod() { return AccessMethod.API_POLL; }

    @Override
    public boolean supportsBulkSubscription() {
        return false;
    }

    @Override
    public Flux<QuoteTick> streamQuotes(Instrument instrument) {
        return Flux.interval(java.time.Duration.ofSeconds(10))
                .map(seq -> new QuoteTick(
                        instrument.symbol(),
                        BigDecimal.valueOf(1.1 + seq * 0.0001),
                        BigDecimal.valueOf(1.1002 + seq * 0.0001),
                        Instant.now(),
                        providerCode()));
    }
}
