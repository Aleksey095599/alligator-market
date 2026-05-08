package com.alligator.market.backend.sourceplan.plan.api.query.options.dto;

import java.util.List;

/**
 * Options response for the market data source plan editor.
 */
public record MarketDataSourcePlanOptionsResponse(
        List<MarketDataCaptureProcessOptionDto> captureProcesses,
        List<InstrumentOptionDto> instruments,
        List<MarketDataSourceOptionDto> sources
) {
}
