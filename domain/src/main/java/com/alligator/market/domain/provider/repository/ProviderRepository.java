package com.alligator.market.domain.provider.repository;

import com.alligator.market.domain.provider.code.ProviderCode;

import java.util.List;

/**
 * Порт репозитория провайдеров.
 */
public interface ProviderRepository {

    /**
     * Найти все коды провайдеров (натуральные ключи).
     */
    List<ProviderCode> findAllCodes();
}
