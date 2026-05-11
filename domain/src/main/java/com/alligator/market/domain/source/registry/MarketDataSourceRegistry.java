package com.alligator.market.domain.source.registry;

import com.alligator.market.domain.source.MarketDataSource;
import com.alligator.market.domain.source.passport.MarketDataSourcePassport;
import com.alligator.market.domain.source.vo.MarketDataSourceCode;

import java.util.Map;

public interface MarketDataSourceRegistry {

    Map<MarketDataSourceCode, MarketDataSource> sourcesByCode();

    Map<MarketDataSourceCode, MarketDataSourcePassport> passportsByCode();
}
