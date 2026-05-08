package com.alligator.market.backend.sourceplan.plan.api.query.options.dto;

/**
 * DTO доступного процесса захвата для dropdown.
 */
public record MarketDataCapturerOptionDto(
        String code,
        String displayName
) {
}
