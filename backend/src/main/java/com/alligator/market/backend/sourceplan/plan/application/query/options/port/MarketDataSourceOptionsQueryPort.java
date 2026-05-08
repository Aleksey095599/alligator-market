package com.alligator.market.backend.sourceplan.plan.application.query.options.port;

import com.alligator.market.domain.source.vo.MarketDataSourceCode;

import java.util.List;

public interface MarketDataSourceOptionsQueryPort {
    List<MarketDataSourceCode> findAllSourceCodes();
}
