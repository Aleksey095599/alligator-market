package com.alligator.market.backend.sourcing.plan.api.query.options.dto;

/**
 * DTO доступного процесса фиксации для dropdown.
 */
public record CaptureProcessOptionDto(
        String code,
        String displayName
) {
}
