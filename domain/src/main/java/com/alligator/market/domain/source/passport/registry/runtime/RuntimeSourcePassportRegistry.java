package com.alligator.market.domain.source.passport.registry.runtime;

import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.vo.SourceCode;

import java.util.Map;

public interface RuntimeSourcePassportRegistry {

    Map<SourceCode, SourcePassport> passportsByCode();
}
