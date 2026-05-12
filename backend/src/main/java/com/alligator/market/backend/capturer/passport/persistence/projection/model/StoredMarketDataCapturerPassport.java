package com.alligator.market.backend.capturer.passport.persistence.projection.model;

import com.alligator.market.domain.capturer.passport.CapturerPassport;
import com.alligator.market.domain.capturer.vo.CapturerCode;

import java.util.Objects;

public record StoredMarketDataCapturerPassport(
        CapturerCode capturerCode,
        CapturerPassport passport,
        StoredMarketDataCapturerProjectionLifecycleStatus lifecycleStatus
) {
    public StoredMarketDataCapturerPassport {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        Objects.requireNonNull(passport, "passport must not be null");
        Objects.requireNonNull(lifecycleStatus, "lifecycleStatus must not be null");
    }
}
