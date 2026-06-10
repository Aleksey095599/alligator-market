package com.alligator.market.backend.source.handler.passport.application.query.list.port;

import com.alligator.market.backend.source.handler.passport.application.query.list.model.SourceHandlerPassportListItem;

import java.util.List;

public interface SourceHandlerPassportListQueryPort {

    List<SourceHandlerPassportListItem> findAll();
}
