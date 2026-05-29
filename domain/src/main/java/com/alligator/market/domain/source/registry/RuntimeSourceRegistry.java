package com.alligator.market.domain.source.registry;

import com.alligator.market.domain.source.MarketDataSource;
import com.alligator.market.domain.source.vo.SourceCode;

import java.util.Map;

public interface RuntimeSourceRegistry {

    Map<SourceCode, MarketDataSource> sourcesByCode();
}
