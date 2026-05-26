package com.alligator.market.backend.source.adapter.twelvedata.instrument.forex.spot.handler;

import com.alligator.market.domain.source.handler.passport.AccessMethod;
import com.alligator.market.domain.source.handler.passport.DeliveryMode;
import com.alligator.market.domain.source.handler.passport.SourceHandlerPassport;

public final class TwelveDataFxSpotHandlerPassport implements SourceHandlerPassport {
    public static final TwelveDataFxSpotHandlerPassport INSTANCE = new TwelveDataFxSpotHandlerPassport();

    private TwelveDataFxSpotHandlerPassport() {
    }

    @Override
    public DeliveryMode deliveryMode() {
        return DeliveryMode.PULL;
    }

    @Override
    public AccessMethod accessMethod() {
        return AccessMethod.API_POLL;
    }
}
