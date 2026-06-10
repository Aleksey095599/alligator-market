package com.alligator.market.backend.source.handler.passport.application.query.list;

import com.alligator.market.backend.source.handler.passport.application.query.list.model.SourceHandlerPassportListItem;
import com.alligator.market.backend.source.handler.passport.application.query.list.port.SourceHandlerPassportListQueryPort;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@Slf4j
public final class SourceHandlerPassportListService {
    private final SourceHandlerPassportListQueryPort queryPort;

    public SourceHandlerPassportListService(SourceHandlerPassportListQueryPort queryPort) {
        this.queryPort = Objects.requireNonNull(queryPort, "queryPort must not be null");
    }

    public List<SourceHandlerPassportListItem> findAll() {
        List<SourceHandlerPassportListItem> passports = queryPort.findAll();
        log.debug("Found {} source handler passport registry rows", passports.size());
        return passports;
    }
}
