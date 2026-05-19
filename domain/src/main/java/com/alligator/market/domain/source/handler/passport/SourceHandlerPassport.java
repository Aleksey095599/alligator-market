package com.alligator.market.domain.source.handler.passport;

import java.util.Objects;

public record SourceHandlerPassport(
        DeliveryMode deliveryMode,
        AccessMethod accessMethod
) {

    public SourceHandlerPassport {
        Objects.requireNonNull(deliveryMode, "deliveryMode must not be null");
        Objects.requireNonNull(accessMethod, "accessMethod must not be null");
    }
}
