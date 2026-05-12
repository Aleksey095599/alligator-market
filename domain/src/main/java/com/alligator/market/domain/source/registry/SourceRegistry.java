package com.alligator.market.domain.source.registry;

import com.alligator.market.domain.source.MarketSource;
import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.vo.SourceCode;

import java.util.Map;

public interface SourceRegistry {

    Map<SourceCode, MarketSource> sourcesByCode();

    Map<SourceCode, SourcePassport> passportsByCode();
}
