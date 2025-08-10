package com.alligator.market.domain.provider.context_sync;

import com.alligator.market.domain.provider.profile.model.ProviderProfile;

import java.util.List;

/**
 * Контракт сканера контекста приложения, извлекающего профили провайдеров рыночных данных (далее - профили).
 */
public interface ProviderContextScanner {

    /**
     * Вернуть список профилей.
     *
     * @throws DuplicateProviderProfileInContextException если в контексте обнаружены дубли
     */
    List<ProviderProfile> getProviderProfiles();
}
