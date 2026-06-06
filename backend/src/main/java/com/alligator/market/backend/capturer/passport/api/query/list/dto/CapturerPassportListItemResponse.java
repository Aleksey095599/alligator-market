package com.alligator.market.backend.capturer.passport.api.query.list.dto;

public record CapturerPassportListItemResponse(
        String capturerCode,
        String displayName,
        String description,
        String registryStatus
) {
}
