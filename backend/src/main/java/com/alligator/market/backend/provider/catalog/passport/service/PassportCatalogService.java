package com.alligator.market.backend.provider.catalog.passport.service;

import com.alligator.market.domain.provider.model.passport.ProviderPassport;

import java.util.List;

/**
 * Application-сервис (use case) для операций с паспортами провайдеров рыночных данных.
 */
public interface PassportCatalogService {

    /**
     * Вернуть все паспорта.
     */
    List<ProviderPassport> findAll();
}
