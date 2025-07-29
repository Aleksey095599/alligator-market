package com.alligator.market.domain.provider.context_sync;

import com.alligator.market.domain.provider.profile.ProviderProfile;

import java.util.List;

/**
 * Порт для сканирования контекста приложения и извлечения профилей провайдеров рыночных данных.
 */
public interface ProviderContextScanner {

    /** Вернуть список профилей провайдеров */
    List<ProviderProfile> getProviderProfiles();
}
