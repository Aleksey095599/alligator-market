package com.alligator.market.backend.marketdata.capture.process.passport.application.query.list;

import com.alligator.market.domain.marketdata.capture.process.passport.MDCaptureProcessPassport;
import com.alligator.market.domain.marketdata.capture.process.registry.MDCaptureProcessRegistry;
import com.alligator.market.domain.marketdata.capture.process.vo.MDCaptureProcessCode;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;

/**
 * Use-case сервис получения списка паспортов процессов захвата рыночных данных.
 */
@Slf4j
public final class MDCaptureProcessPassportListService {

    /* Доменный реестр процессов захвата — источник истины по активным паспортам. */
    private final MDCaptureProcessRegistry registry;

    public MDCaptureProcessPassportListService(MDCaptureProcessRegistry registry) {
        this.registry = Objects.requireNonNull(registry, "registry must not be null");
    }

    /**
     * Возвращает все паспорта процессов захвата.
     */
    public Map<MDCaptureProcessCode, MDCaptureProcessPassport> findAll() {
        Map<MDCaptureProcessCode, MDCaptureProcessPassport> passports = registry.passportsByCode();
        log.debug("Found {} capture process passports", passports.size());
        return passports;
    }
}
