package com.alligator.market.backend.marketdata.capture.process.passport.application.query.list;

import com.alligator.market.domain.marketdata.capture.process.passport.CaptureProcessPassport;
import com.alligator.market.domain.marketdata.capture.process.registry.CaptureProcessRegistry;
import com.alligator.market.domain.marketdata.capture.process.vo.CaptureProcessCode;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;

/**
 * Use-case сервис получения списка паспортов процессов фиксации рыночных данных.
 */
@Slf4j
public final class CaptureProcessPassportListService {

    /* Доменный реестр процессов фиксации — источник истины по активным паспортам. */
    private final CaptureProcessRegistry registry;

    public CaptureProcessPassportListService(CaptureProcessRegistry registry) {
        this.registry = Objects.requireNonNull(registry, "registry must not be null");
    }

    /**
     * Возвращает все паспорта процессов фиксации.
     */
    public Map<CaptureProcessCode, CaptureProcessPassport> findAll() {
        Map<CaptureProcessCode, CaptureProcessPassport> passports = registry.passportsByCode();
        log.debug("Found {} capture process passports", passports.size());
        return passports;
    }
}
