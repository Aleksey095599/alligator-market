package com.alligator.market.domain.provider.repository.passport;

import com.alligator.market.domain.provider.code.ProviderCode;

import java.util.List;

/**
 * Порт репозитория, где хранятся паспорта провайдеров.
 */
public interface ProviderPassportRepository {

    /**
     * Найти все коды провайдеров.
     */
    List<ProviderCode> findAllCodes();
}
