package com.alligator.market.backend.sourcing.plan.api.query.common.mapper;

import com.alligator.market.backend.sourcing.plan.api.query.common.dto.MarketDataSourceResponse;
import com.alligator.market.backend.sourcing.plan.application.query.common.model.MarketDataSourceQueryItem;

public class MarketDataSourceResponseMapper {

    public MarketDataSourceResponse toSourceResponse(MarketDataSourceQueryItem source) {
        return new MarketDataSourceResponse(
                source.providerCode(),
                source.priority(),
                source.lifecycleStatus()
        );
    }
}
