package com.alligator.market.backend.capturer.passport.application.projection.port;

import com.alligator.market.domain.capturer.passport.MarketDataCapturerPassport;
import com.alligator.market.domain.capturer.registry.MarketDataCapturerRegistry;
import com.alligator.market.domain.capturer.vo.MarketDataCapturerCode;

import java.util.Map;
import java.util.Set;

public interface MarketDataCapturerPassportProjectionWritePort {
    void retireAllExcept(Set<MarketDataCapturerCode> currentCodes);

    void upsertAll(Map<MarketDataCapturerCode, MarketDataCapturerPassport> passports);
}
