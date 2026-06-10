package com.alligator.market.backend.source.handler.passport.application.query.list.model;

import com.alligator.market.domain.source.handler.passport.AccessMethod;
import com.alligator.market.domain.source.handler.passport.DeliveryMode;
import com.alligator.market.domain.source.handler.passport.store.SourceHandlerPassportRecord;

import java.util.Objects;

public record SourceHandlerPassportListItem(
        String sourceCode,
        String handlerCode,
        DeliveryMode deliveryMode,
        AccessMethod accessMethod,
        SourceHandlerPassportRecord.RegistryStatus registryStatus
) {
    public SourceHandlerPassportListItem {
        Objects.requireNonNull(sourceCode, "sourceCode must not be null");
        Objects.requireNonNull(handlerCode, "handlerCode must not be null");
        Objects.requireNonNull(deliveryMode, "deliveryMode must not be null");
        Objects.requireNonNull(accessMethod, "accessMethod must not be null");
        Objects.requireNonNull(registryStatus, "registryStatus must not be null");
    }
}
