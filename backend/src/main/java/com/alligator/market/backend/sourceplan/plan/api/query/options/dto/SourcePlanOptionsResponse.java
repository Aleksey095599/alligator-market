package com.alligator.market.backend.sourceplan.plan.api.query.options.dto;

import java.util.List;

/**
 * Options response for the source plan editor.
 */
public record SourcePlanOptionsResponse(
        List<MarketDataCapturerOptionDto> capturers,
        List<InstrumentOptionDto> instruments,
        List<MarketDataSourceOptionDto> sources
) {
}
