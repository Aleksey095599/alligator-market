package com.alligator.market.backend.source.passport.application.query.list.port;

import com.alligator.market.backend.source.passport.application.query.list.model.MarketDataSourcePassportListItem;

import java.util.List;

public interface MarketDataSourcePassportListQueryPort {
    List<MarketDataSourcePassportListItem> findAll();
}
