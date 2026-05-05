package com.alligator.market.backend.marketdata.capture.process.passport.application.query.list;

import com.alligator.market.domain.marketdata.capture.process.passport.MarketDataCaptureProcessPassport;
import com.alligator.market.domain.marketdata.capture.process.registry.MarketDataCaptureProcessRegistry;
import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessCode;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;

/**
 * Use-case сервис получения списка паспортов процессов захвата рыночных данных.
 */
@Slf4j
public final class MarketDataCaptureProcessPassportListService {

    /* Доменный реестр процессов захвата — источник истины по активным паспортам. */
    private final MarketDataCaptureProcessRegistry registry;

    public MarketDataCaptureProcessPassportListService(MarketDataCaptureProcessRegistry registry) {
        this.registry = Objects.requireNonNull(registry, "registry must not be null");
    }

    /**
     * Возвращает все паспорта процессов захвата.
     */
    public Map<MarketDataCaptureProcessCode, MarketDataCaptureProcessPassport> findAll() {
        Map<MarketDataCaptureProcessCode, MarketDataCaptureProcessPassport> passports = registry.passportsByCode();
        log.debug("Found {} capture process passports", passports.size());
        return passports;
    }
}
