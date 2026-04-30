package com.alligator.market.backend.provider.application.passport.catalog;

import com.alligator.market.domain.provider.passport.ProviderPassport;
import com.alligator.market.domain.provider.registry.ProviderRegistry;
import com.alligator.market.domain.provider.vo.ProviderCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

/**
 * Реализация сервиса {@link PassportCatalogService}.
 *
 * <p>Чтение выполняется напрямую из доменного реестра провайдеров.</p>
 */
public final class PassportCatalogServiceImpl implements PassportCatalogService {

    private static final Logger log = LoggerFactory.getLogger(PassportCatalogServiceImpl.class);

    /* Доменный реестр провайдеров — источник истины по активным паспортам. */
    private final ProviderRegistry providerRegistry;

    public PassportCatalogServiceImpl(ProviderRegistry providerRegistry) {
        this.providerRegistry = Objects.requireNonNull(providerRegistry, "providerRegistry must not be null");
    }

    @Override
    public Map<ProviderCode, ProviderPassport> findAll() {
        Map<ProviderCode, ProviderPassport> passports = providerRegistry.passportsByCode();

        log.debug("Found {} provider passports", passports.size());
        return passports;
    }
}
