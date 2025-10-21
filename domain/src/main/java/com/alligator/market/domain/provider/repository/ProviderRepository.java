package com.alligator.market.domain.provider.repository;

import java.util.List;

/**
 * Порт репозитория провайдеров.
 */
public interface ProviderRepository {

    /** Найти все коды провайдеров (натуральные ключи). */
    List<String> findAllCodes();
}
