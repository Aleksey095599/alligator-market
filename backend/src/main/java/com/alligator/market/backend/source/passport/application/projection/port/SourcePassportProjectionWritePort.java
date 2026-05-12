package com.alligator.market.backend.source.passport.application.projection.port;

import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.vo.SourceCode;

import java.util.Map;
import java.util.Set;

public interface SourcePassportProjectionWritePort {
    void retireAllExcept(Set<SourceCode> passportCodes);

    void upsertAll(Map<SourceCode, SourcePassport> passports);
}
