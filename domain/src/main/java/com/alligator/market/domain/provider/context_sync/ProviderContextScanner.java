package com.alligator.market.domain.provider.context_sync;

import com.alligator.market.domain.provider.profile.ProviderProfile;

import java.util.List;

/**
 * Контракт сканера контекста приложения, извлекающего профили провайдеров.
 */
public interface ProviderContextScanner {

    /**
     * Вернуть список профилей провайдеров.
     *
     * @throws DuplicateProviderProfileInContextException если в контексте обнаружены дубли
     */
    List<ProviderProfile> getProviderProfiles();
}
