package com.alligator.market.backend.provider.catalog.service;

import java.util.List;

/**
 * Application-сервис (use case) для чтения каталога провайдеров рыночных данных.
 */
public interface ProviderCatalogUseCase {

    /** Вернуть все провайдеры с дескрипторами и параметрами. */
    List<ProviderCatalogItem> findAll();
}
