package com.alligator.market.backend.source.passport.application.query.list;

import com.alligator.market.backend.source.passport.application.query.list.model.ProviderPassportListItem;
import com.alligator.market.backend.source.passport.application.query.list.port.ProviderPassportListQueryPort;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

/**
 * Use-case service for reading provider passport projection rows.
 */
@Slf4j
public final class PassportListService {

    private final ProviderPassportListQueryPort queryPort;

    public PassportListService(ProviderPassportListQueryPort queryPort) {
        this.queryPort = Objects.requireNonNull(queryPort, "queryPort must not be null");
    }

    /**
     * Returns all provider passport projection rows.
     */
    public List<ProviderPassportListItem> findAll() {
        List<ProviderPassportListItem> passports = queryPort.findAll();
        log.debug("Found {} provider passport projection rows", passports.size());
        return passports;
    }
}
