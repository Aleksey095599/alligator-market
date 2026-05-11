package com.alligator.market.backend.source.passport.persistence.projection.mapper;

import com.alligator.market.backend.source.passport.persistence.projection.model.StoredMarketDataSourcePassport;
import com.alligator.market.domain.source.passport.MarketDataSourcePassport;
import com.alligator.market.domain.source.vo.MarketDataSourceCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.alligator.market.backend.source.passport.persistence.projection.model.StoredMarketDataSourceProjectionLifecycleStatus.ACTIVE;

public final class StoredMarketDataSourcePassportMapper {

    public List<StoredMarketDataSourcePassport> toActiveStored(
            Map<MarketDataSourceCode, MarketDataSourcePassport> passports
    ) {
        if (passports == null) {
            throw new IllegalArgumentException("passports must not be null");
        }

        List<StoredMarketDataSourcePassport> storedPassports = new ArrayList<>(passports.size());
        for (Map.Entry<MarketDataSourceCode, MarketDataSourcePassport> entry : passports.entrySet()) {
            MarketDataSourceCode sourceCode = entry.getKey();
            if (sourceCode == null) {
                throw new IllegalArgumentException("passports must not contain null keys");
            }

            MarketDataSourcePassport passport = entry.getValue();
            if (passport == null) {
                throw new IllegalArgumentException("passports must not contain null values");
            }

            storedPassports.add(toActiveStored(sourceCode, passport));
        }

        return storedPassports;
    }

    public StoredMarketDataSourcePassport toActiveStored(
            MarketDataSourceCode sourceCode,
            MarketDataSourcePassport passport
    ) {
        return new StoredMarketDataSourcePassport(sourceCode, passport, ACTIVE);
    }
}
