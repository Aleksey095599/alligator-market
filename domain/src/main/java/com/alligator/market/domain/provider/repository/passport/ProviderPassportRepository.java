package com.alligator.market.domain.provider.repository.passport;

import com.alligator.market.domain.provider.code.ProviderCode;

import java.util.List;

/**
 * Порт репозитория паспортов провайдеров.
 */
public interface ProviderPassportRepository {

    /**
     * Найти все коды провайдеров.
     */
    List<ProviderCode> findAllCodes();
}
