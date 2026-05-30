package com.alligator.market.domain.source.passport.registry.stored;

import com.alligator.market.domain.source.vo.SourceCode;

import java.util.Collection;
import java.util.Set;

public interface StoredSourcePassportRegistry {

    void retireAllExcept(Set<SourceCode> registeredSourceCodes);

    void save(Collection<StoredSourcePassport> passports);
}
