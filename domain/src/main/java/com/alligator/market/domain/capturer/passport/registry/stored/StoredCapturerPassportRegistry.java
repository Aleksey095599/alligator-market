package com.alligator.market.domain.capturer.passport.registry.stored;

import com.alligator.market.domain.capturer.vo.CapturerCode;

import java.util.Collection;
import java.util.Set;

public interface StoredCapturerPassportRegistry {

    void retireAllExcept(Set<CapturerCode> registeredCapturerCodes);

    void save(Collection<StoredCapturerPassport> passports);
}
