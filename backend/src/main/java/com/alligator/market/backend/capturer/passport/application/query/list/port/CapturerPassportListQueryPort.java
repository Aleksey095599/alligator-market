package com.alligator.market.backend.capturer.passport.application.query.list.port;

import com.alligator.market.backend.capturer.passport.application.query.list.model.CapturerPassportListItem;

import java.util.List;

public interface CapturerPassportListQueryPort {
    List<CapturerPassportListItem> findAll();
}
