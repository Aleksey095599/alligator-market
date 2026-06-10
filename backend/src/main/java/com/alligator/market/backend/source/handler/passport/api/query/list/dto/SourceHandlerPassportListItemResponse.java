package com.alligator.market.backend.source.handler.passport.api.query.list.dto;

public record SourceHandlerPassportListItemResponse(
        String sourceCode,
        String handlerCode,
        String deliveryMode,
        String accessMethod,
        String registryStatus
) {
}
