package com.alligator.market.backend.capturer.passport.persistence.projection.mapper;

import com.alligator.market.backend.capturer.passport.persistence.projection.model.StoredMarketDataCapturerPassport;
import com.alligator.market.domain.capturer.passport.MarketDataCapturerPassport;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.alligator.market.backend.capturer.passport.persistence.projection.model.StoredMarketDataCapturerProjectionLifecycleStatus.ACTIVE;

public final class StoredMarketDataCapturerPassportMapper {

    public List<StoredMarketDataCapturerPassport> toActiveStored(
            Map<MarketDataCapturerCode, MarketDataCapturerPassport> passports
    ) {
        if (passports == null) {
            throw new IllegalArgumentException("passports must not be null");
        }

        List<StoredMarketDataCapturerPassport> storedPassports = new ArrayList<>(passports.size());
        for (Map.Entry<MarketDataCapturerCode, MarketDataCapturerPassport> entry : passports.entrySet()) {
            MarketDataCapturerCode capturerCode = entry.getKey();
            if (capturerCode == null) {
                throw new IllegalArgumentException("passports must not contain null keys");
            }

            MarketDataCapturerPassport passport = entry.getValue();
            if (passport == null) {
                throw new IllegalArgumentException("passports must not contain null values");
            }

            storedPassports.add(toActiveStored(capturerCode, passport));
        }

        return storedPassports;
    }

    public StoredMarketDataCapturerPassport toActiveStored(
            MarketDataCapturerCode capturerCode,
            MarketDataCapturerPassport passport
    ) {
        return new StoredMarketDataCapturerPassport(capturerCode, passport, ACTIVE);
    }
}
