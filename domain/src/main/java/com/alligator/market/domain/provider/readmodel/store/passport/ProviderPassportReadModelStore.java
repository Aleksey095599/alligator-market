package com.alligator.market.domain.provider.readmodel.store.passport;

import com.alligator.market.domain.provider.model.vo.ProviderCode;

import java.util.List;

/**
 * Репозиторий паспортов провайдеров.
 */
public interface ProviderPassportReadModelStore {

    /**
     * Извлечь коды провайдеров.
     */
    List<ProviderCode> findAllCodes();
}
