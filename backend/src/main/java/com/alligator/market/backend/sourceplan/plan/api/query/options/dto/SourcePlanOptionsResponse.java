package com.alligator.market.backend.sourceplan.plan.api.query.options.dto;

import java.util.List;

public record SourcePlanOptionsResponse(
        List<MarketDataCapturerOptionDto> capturers,
        List<InstrumentOptionDto> instruments,
        List<MarketDataSourceOptionDto> sources
) {
}
