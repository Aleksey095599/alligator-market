package com.alligator.market.domain.provider;

import com.alligator.market.domain.provider.profile.ProviderProfile;

import java.util.Map;

/**
 * Порт хранилища профилей провайдеров.
 */
public interface ProviderProfileRepository {

    /** Вернуть все профили провайдеров вместе с PK */
    Map<ProviderProfile, Long> findAll();
}
