package com.alligator.market.domain.provider.reconciliation;

import com.alligator.market.domain.provider.profile.model.Profile;

import java.util.List;

/**
 * Контракт сканера контекста приложения, извлекающий профили провайдеров.
 */
public interface ProfileContextScanner {

    /** Вернуть список профилей из контекста. */
    List<Profile> getProfiles();
}
