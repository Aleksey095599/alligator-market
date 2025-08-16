package com.alligator.market.domain.provider.profile.context;

import com.alligator.market.domain.provider.profile.model.ProviderProfile;

import java.util.List;

/**
 * Контракт сканера контекста приложения, извлекающего профили провайдеров рыночных данных (далее - профили).
 */
public interface ProfileContextScanner {

    /**
     * Вернуть список профилей.
     *
     * @throws DuplicateProfileInContextException если в контексте обнаружены дубли
     */
    List<ProviderProfile> getProviderProfiles();
}
