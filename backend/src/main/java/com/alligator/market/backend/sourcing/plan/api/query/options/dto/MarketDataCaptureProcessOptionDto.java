package com.alligator.market.backend.sourcing.plan.api.query.options.dto;

/**
 * DTO доступного процесса захвата для dropdown.
 */
public record MarketDataCaptureProcessOptionDto(
        String code,
        String displayName
) {
}
