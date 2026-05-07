package com.alligator.market.backend.sourceplan.plan.api.query.options.dto;

import java.util.List;

/**
 * Ответ с options для экрана управления планами источников.
 */
public record MarketDataSourcePlanOptionsResponse(
        List<MarketDataCaptureProcessOptionDto> captureProcesses,
        List<InstrumentOptionDto> instruments,
        List<MarketDataSourceOptionDto> providers
) {
}
