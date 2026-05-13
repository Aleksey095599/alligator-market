package com.alligator.market.domain.capturer.passport.registry;

import com.alligator.market.domain.capturer.passport.CapturerPassport;
import com.alligator.market.domain.capturer.vo.CapturerCode;

import java.util.Map;

public interface RuntimeCapturerPassportRegistry {

    Map<CapturerCode, CapturerPassport> passportsByCode();
}
