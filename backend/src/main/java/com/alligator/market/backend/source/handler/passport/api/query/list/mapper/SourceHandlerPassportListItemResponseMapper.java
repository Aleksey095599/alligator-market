package com.alligator.market.backend.source.handler.passport.api.query.list.mapper;

import com.alligator.market.backend.source.handler.passport.api.query.list.dto.SourceHandlerPassportListItemResponse;
import com.alligator.market.backend.source.handler.passport.application.query.list.model.SourceHandlerPassportListItem;

public final class SourceHandlerPassportListItemResponseMapper {

    private SourceHandlerPassportListItemResponseMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static SourceHandlerPassportListItemResponse toResponse(SourceHandlerPassportListItem item) {
        return new SourceHandlerPassportListItemResponse(
                item.sourceCode(),
                item.handlerCode(),
                item.deliveryMode().code(),
                item.accessMethod().code(),
                item.registryStatus().name()
        );
    }
}
