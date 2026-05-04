package com.alligator.market.backend.sourcing.plan.api.query.options.dto;

import java.util.List;

/**
 * Ответ с options для экрана управления планами источников.
 */
public record MarketDataSourcePlanOptionsResponse(
        List<CaptureProcessOptionDto> captureProcesses,
        List<InstrumentOptionDto> instruments,
        List<ProviderOptionDto> providers
) {
}
