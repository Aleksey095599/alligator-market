package com.alligator.market.backend.provider.catalog.passport.service;

import java.util.List;

/**
 * Application-сервис (use case) для чтения каталога провайдеров рыночных данных.
 */
public interface ProviderCatalogUseCase {

    /**
     * Вернуть все провайдеры с паспортами и параметрами.
     */
    List<ProviderCatalogItem> findAll();
}
