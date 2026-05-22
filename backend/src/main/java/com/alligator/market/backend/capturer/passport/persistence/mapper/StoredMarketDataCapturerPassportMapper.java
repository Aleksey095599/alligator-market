package com.alligator.market.backend.capturer.passport.persistence.mapper;

import com.alligator.market.backend.capturer.passport.persistence.model.StoredMarketDataCapturerPassport;
import com.alligator.market.domain.capturer.passport.CapturerPassport;
import com.alligator.market.domain.capturer.vo.CapturerCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.alligator.market.domain.capturer.passport.registry.stored.StoredCapturerPassportRegistryStatus.REGISTERED;

public final class StoredMarketDataCapturerPassportMapper {

    public List<StoredMarketDataCapturerPassport> toRegisteredStored(
            Map<CapturerCode, CapturerPassport> passports
    ) {
        if (passports == null) {
            throw new IllegalArgumentException("passports must not be null");
        }

        List<StoredMarketDataCapturerPassport> storedPassports = new ArrayList<>(passports.size());
        for (Map.Entry<CapturerCode, CapturerPassport> entry : passports.entrySet()) {
            CapturerCode capturerCode = entry.getKey();
            if (capturerCode == null) {
                throw new IllegalArgumentException("passports must not contain null keys");
            }

            CapturerPassport passport = entry.getValue();
            if (passport == null) {
                throw new IllegalArgumentException("passports must not contain null values");
            }

            storedPassports.add(toRegisteredStored(capturerCode, passport));
        }

        return storedPassports;
    }

    public StoredMarketDataCapturerPassport toRegisteredStored(
            CapturerCode capturerCode,
            CapturerPassport passport
    ) {
        return new StoredMarketDataCapturerPassport(capturerCode, passport, REGISTERED);
    }
}
