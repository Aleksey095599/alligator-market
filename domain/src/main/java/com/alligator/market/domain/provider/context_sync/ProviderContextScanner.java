package com.alligator.market.domain.provider.context_sync;

import com.alligator.market.domain.provider.profile.ProviderProfile;

import java.util.List;

/**
 * Порт поиска в контексте Spring всех адаптеров провайдеров рыночных данных
 * и извлечения их профилей.
 */
public interface ProviderContextScanner {

    /** Вернуть список профилей провайдеров */
    List<ProviderProfile> getProviderProfiles();
}
