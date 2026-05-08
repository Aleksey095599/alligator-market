package com.alligator.market.backend.source.passport.application.projection.port;

import com.alligator.market.domain.source.passport.MarketDataSourcePassport;
import com.alligator.market.domain.source.vo.MarketDataSourceCode;
import com.alligator.market.domain.source.registry.MarketDataSourceRegistry;

import java.util.Map;
import java.util.Set;

public interface MarketDataSourcePassportProjectionWritePort {
    void retireAllExcept(Set<MarketDataSourceCode> currentCodes);

    void upsertAll(Map<MarketDataSourceCode, MarketDataSourcePassport> passports);
}
