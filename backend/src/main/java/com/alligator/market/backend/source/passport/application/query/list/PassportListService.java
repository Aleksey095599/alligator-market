package com.alligator.market.backend.source.passport.application.query.list;

import com.alligator.market.backend.source.passport.application.query.list.model.SourcePassportListItem;
import com.alligator.market.backend.source.passport.application.query.list.port.SourcePassportListQueryPort;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@Slf4j
public final class PassportListService {
    private final SourcePassportListQueryPort queryPort;

    public PassportListService(SourcePassportListQueryPort queryPort) {
        this.queryPort = Objects.requireNonNull(queryPort, "queryPort must not be null");
    }

    public List<SourcePassportListItem> findAll() {
        List<SourcePassportListItem> passports = queryPort.findAll();
        log.debug("Found {} source passport projection rows", passports.size());
        return passports;
    }
}
