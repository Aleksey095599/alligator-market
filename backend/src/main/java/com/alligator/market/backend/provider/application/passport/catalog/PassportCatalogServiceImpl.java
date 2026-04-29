package com.alligator.market.backend.provider.application.passport.catalog;

import com.alligator.market.domain.provider.passport.ProviderPassport;
import com.alligator.market.domain.provider.vo.ProviderCode;
import com.alligator.market.backend.provider.application.passport.catalog.port.out.ProviderPassportQueryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

/**
 * Реализация сервиса {@link PassportCatalogService}.
 *
 * <p>Чтение выполняется через доменный query-порт materialized view.</p>
 */
public final class PassportCatalogServiceImpl implements PassportCatalogService {

    private static final Logger log = LoggerFactory.getLogger(PassportCatalogServiceImpl.class);

    /* Query-порт чтения паспортов провайдеров из read model (provider_passport). */
    private final ProviderPassportQueryPort queryPort;

    public PassportCatalogServiceImpl(ProviderPassportQueryPort queryPort) {
        this.queryPort = Objects.requireNonNull(queryPort, "queryPort must not be null");
    }

    @Override
    public Map<ProviderCode, ProviderPassport> findAll() {
        Map<ProviderCode, ProviderPassport> passports = queryPort.findAll();

        log.debug("Found {} provider passports", passports.size());
        return passports;
    }
}
