package com.alligator.market.backend.capturer.passport.application.query.list.port;

import com.alligator.market.backend.capturer.passport.application.query.list.model.MarketDataCapturerPassportListItem;

import java.util.List;

public interface MarketDataCapturerPassportListQueryPort {
    List<MarketDataCapturerPassportListItem> findAll();
}
