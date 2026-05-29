package com.alligator.market.backend.capturer.passport.application.query.list;

import com.alligator.market.backend.capturer.passport.application.query.list.model.CapturerPassportListItem;
import com.alligator.market.backend.capturer.passport.application.query.list.port.CapturerPassportListQueryPort;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@Slf4j
public final class CapturerPassportListService {
    private final CapturerPassportListQueryPort queryPort;

    public CapturerPassportListService(
            CapturerPassportListQueryPort queryPort
    ) {
        this.queryPort = Objects.requireNonNull(queryPort, "queryPort must not be null");
    }

    public List<CapturerPassportListItem> findAll() {
        List<CapturerPassportListItem> passports = queryPort.findAll();
        log.debug("Found {} capturer passport registry rows", passports.size());
        return passports;
    }
}
