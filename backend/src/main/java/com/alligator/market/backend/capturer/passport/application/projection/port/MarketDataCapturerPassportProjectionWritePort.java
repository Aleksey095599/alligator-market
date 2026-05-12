package com.alligator.market.backend.capturer.passport.application.projection.port;

import com.alligator.market.domain.capturer.passport.CapturerPassport;
import com.alligator.market.domain.capturer.vo.CapturerCode;

import java.util.Map;
import java.util.Set;

public interface MarketDataCapturerPassportProjectionWritePort {
    void retireAllExcept(Set<CapturerCode> passportCodes);

    void upsertAll(Map<CapturerCode, CapturerPassport> passports);
}
