package com.alligator.market.domain.provider.repository.passport;

import com.alligator.market.domain.provider.model.vo.ProviderCode;

import java.util.List;

/**
 * Репозиторий паспортов провайдеров.
 */
public interface ProviderPassportRepository {

    /**
     * Извлечь коды провайдеров.
     */
    List<ProviderCode> findAllCodes();
}
