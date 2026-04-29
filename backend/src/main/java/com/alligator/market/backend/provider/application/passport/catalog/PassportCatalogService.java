package com.alligator.market.backend.provider.application.passport.catalog;

import com.alligator.market.domain.provider.passport.ProviderPassport;
import com.alligator.market.domain.provider.vo.ProviderCode;

import java.util.Map;

/**
 * Application-сервис (use case) для операций с паспортами провайдеров рыночных данных.
 */
public interface PassportCatalogService {

    /**
     * Вернуть все паспорта.
     */
    Map<ProviderCode, ProviderPassport> findAll();
}
