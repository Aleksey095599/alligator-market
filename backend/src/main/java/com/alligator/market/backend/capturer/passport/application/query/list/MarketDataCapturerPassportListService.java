package com.alligator.market.backend.capturer.passport.application.query.list;

import com.alligator.market.backend.capturer.passport.application.query.list.model.MarketDataCapturerPassportListItem;
import com.alligator.market.backend.capturer.passport.application.query.list.port.MarketDataCapturerPassportListQueryPort;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@Slf4j
public final class MarketDataCapturerPassportListService {
    private final MarketDataCapturerPassportListQueryPort queryPort;

    public MarketDataCapturerPassportListService(
            MarketDataCapturerPassportListQueryPort queryPort
    ) {
        this.queryPort = Objects.requireNonNull(queryPort, "queryPort must not be null");
    }

    public List<MarketDataCapturerPassportListItem> findAll() {
        List<MarketDataCapturerPassportListItem> passports = queryPort.findAll();
        log.debug("Found {} capturer passport projection rows", passports.size());
        return passports;
    }
}
