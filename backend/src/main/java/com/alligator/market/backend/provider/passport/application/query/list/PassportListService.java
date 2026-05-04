package com.alligator.market.backend.provider.passport.application.query.list;

import com.alligator.market.domain.provider.passport.ProviderPassport;
import com.alligator.market.domain.provider.registry.ProviderRegistry;
import com.alligator.market.domain.provider.vo.ProviderCode;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;

/**
 * Use-case сервис получения списка паспортов провайдеров.
 */
@Slf4j
public final class PassportListService {

    /* Доменный реестр провайдеров — источник истины по активным паспортам. */
    private final ProviderRegistry providerRegistry;

    public PassportListService(ProviderRegistry providerRegistry) {
        this.providerRegistry = Objects.requireNonNull(providerRegistry, "providerRegistry must not be null");
    }

    /**
     * Возвращает все паспорта провайдеров.
     */
    public Map<ProviderCode, ProviderPassport> findAll() {
        Map<ProviderCode, ProviderPassport> passports = providerRegistry.passportsByCode();
        log.debug("Found {} provider passports", passports.size());
        return passports;
    }
}
