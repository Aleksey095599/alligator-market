package com.alligator.market.backend.capturer.passport.persistence.model;

import com.alligator.market.domain.capturer.passport.CapturerPassport;
import com.alligator.market.domain.capturer.passport.registry.stored.StoredCapturerPassportRegistryStatus;
import com.alligator.market.domain.capturer.vo.CapturerCode;

import java.util.Objects;

public record StoredCapturerPassport(
        CapturerCode capturerCode,
        CapturerPassport passport,
        StoredCapturerPassportRegistryStatus registryStatus
) {
    public StoredCapturerPassport {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        Objects.requireNonNull(passport, "passport must not be null");
        Objects.requireNonNull(registryStatus, "registryStatus must not be null");
    }
}
