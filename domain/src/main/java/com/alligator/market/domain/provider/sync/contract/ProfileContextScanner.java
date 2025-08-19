package com.alligator.market.domain.provider.sync.contract;

import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import com.alligator.market.domain.provider.sync.exeption.ContextProfileDuplicateException;

import java.util.List;

/**
 * Контракт сканера контекста приложения, извлекающего профили провайдеров рыночных данных.
 */
public interface ProfileContextScanner {

    /**
     * Вернуть список профилей.
     *
     * @throws ContextProfileDuplicateException если в контексте обнаружены дубли
     */
    List<ProviderProfile> getProviderProfiles();
}
