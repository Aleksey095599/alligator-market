package com.alligator.market.backend.source.passport.application.query.list.port;

import com.alligator.market.backend.source.passport.application.query.list.model.SourcePassportListItem;

import java.util.List;

public interface SourcePassportListQueryPort {
    List<SourcePassportListItem> findAll();
}
