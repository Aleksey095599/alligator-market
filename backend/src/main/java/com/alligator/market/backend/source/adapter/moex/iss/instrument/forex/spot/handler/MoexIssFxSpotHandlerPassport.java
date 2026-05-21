package com.alligator.market.backend.source.adapter.moex.iss.instrument.forex.spot.handler;

import com.alligator.market.domain.source.handler.passport.AccessMethod;
import com.alligator.market.domain.source.handler.passport.DeliveryMode;
import com.alligator.market.domain.source.handler.passport.SourceHandlerPassport;

public final class MoexIssFxSpotHandlerPassport implements SourceHandlerPassport {
    public static final MoexIssFxSpotHandlerPassport INSTANCE = new MoexIssFxSpotHandlerPassport();

    private MoexIssFxSpotHandlerPassport() {
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
