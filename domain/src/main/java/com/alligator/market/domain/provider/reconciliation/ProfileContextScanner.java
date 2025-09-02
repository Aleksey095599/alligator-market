package com.alligator.market.domain.provider.reconciliation;

import com.alligator.market.domain.provider.profile.model.Profile;

import java.util.Map;

/**
 * Контракт сканера контекста приложения, извлекающий профили провайдеров.
 */
public interface ProfileContextScanner {

    /**
     * Вернуть карту профилей из контекста, где ключ — код провайдера.
     */
    Map<String, Profile> getProfiles();
}
