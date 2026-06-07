package com.alligator.market.domain.capturer.passport.store;

import com.alligator.market.domain.capturer.vo.CapturerCode;

import java.util.Collection;
import java.util.Set;

public interface CapturerPassportStore {

    void retireAllExcept(Set<CapturerCode> registeredCapturerCodes);

    void save(Collection<CapturerPassportRecord> passports);
}
