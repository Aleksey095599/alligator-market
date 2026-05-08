package com.alligator.market.backend.source.passport.application.query.list;

import com.alligator.market.backend.source.passport.application.query.list.model.MarketDataSourcePassportListItem;
import com.alligator.market.backend.source.passport.application.query.list.port.MarketDataSourcePassportListQueryPort;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@Slf4j
public final class PassportListService {
    private final MarketDataSourcePassportListQueryPort queryPort;

    public PassportListService(MarketDataSourcePassportListQueryPort queryPort) {
        this.queryPort = Objects.requireNonNull(queryPort, "queryPort must not be null");
    }

    public List<MarketDataSourcePassportListItem> findAll() {
        List<MarketDataSourcePassportListItem> passports = queryPort.findAll();
        log.debug("Found {} source passport projection rows", passports.size());
        return passports;
    }
}
