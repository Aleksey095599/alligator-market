package com.alligator.market.backend.provider;

import com.alligator.market.domain.instrument.forex.Instrument;
import com.alligator.market.domain.provider.AccessMethod;
import com.alligator.market.domain.provider.DeliveryMode;
import com.alligator.market.domain.provider.MarketDataProvider;
import com.alligator.market.domain.quotes.QuoteTick;
import reactor.core.publisher.Flux;

public class TwelveFreeAdapter2 implements MarketDataProvider {

    @Override
    public String code() { return "TWELVE_FREE2"; }

    @Override
    public DeliveryMode deliveryMode() { return DeliveryMode.PULL; }

    @Override
    public AccessMethod accessMethod() { return AccessMethod.API_POLL; }

    @Override
    public boolean supportsBulkSubscription() { return false; }

    @Override
    public Flux<QuoteTick> subscribe(Instrument instrument) {
        return null;
    }
}
