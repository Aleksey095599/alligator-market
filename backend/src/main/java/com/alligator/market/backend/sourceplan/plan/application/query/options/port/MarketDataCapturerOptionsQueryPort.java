package com.alligator.market.backend.sourceplan.plan.application.query.options.port;

import com.alligator.market.backend.sourceplan.plan.application.query.options.model.MarketDataCapturerOption;

import java.util.List;

public interface MarketDataCapturerOptionsQueryPort {
    List<MarketDataCapturerOption> findAllMarketDataCapturers();
}
