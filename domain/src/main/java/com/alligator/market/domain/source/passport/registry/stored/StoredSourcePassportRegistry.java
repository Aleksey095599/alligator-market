package com.alligator.market.domain.source.passport.registry.stored;

import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.vo.SourceCode;

import java.util.Map;
import java.util.Set;

public interface StoredSourcePassportRegistry {

    void retireAllExcept(Set<SourceCode> activeSourceCodes);

    void saveActive(Map<SourceCode, SourcePassport> activePassports);
}
