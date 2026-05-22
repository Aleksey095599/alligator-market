package com.alligator.market.domain.capturer.passport.registry.stored;

import com.alligator.market.domain.capturer.passport.CapturerPassport;
import com.alligator.market.domain.capturer.vo.CapturerCode;

import java.util.Map;
import java.util.Set;

public interface StoredCapturerPassportRegistry {

    void retireAllExcept(Set<CapturerCode> registeredCapturerCodes);

    void saveRegistered(Map<CapturerCode, CapturerPassport> registeredPassports);
}
