package com.alligator.market.domain.source.passport.store;

import com.alligator.market.domain.source.vo.SourceCode;

import java.util.Collection;
import java.util.Set;

public interface SourcePassportStore {

    void retireAllExcept(Set<SourceCode> registeredSourceCodes);

    void save(Collection<SourcePassportRecord> passports);
}
