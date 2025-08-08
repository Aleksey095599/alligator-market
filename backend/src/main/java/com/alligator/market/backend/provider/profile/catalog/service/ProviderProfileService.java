package com.alligator.market.backend.provider.profile.catalog.service;

import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import com.alligator.market.domain.provider.profile.model.ProviderProfileStatus;
import java.util.Map;

/**
 * Сервис профилей провайдеров.
 */
public interface ProviderProfileService {

    /** Вернуть все активные профили провайдеров вместе с PK. */
    Map<ProviderProfile, Long> findAllActive();

    /** Вернуть все профили провайдеров с их статусами. */
    Map<ProviderProfile, ProviderProfileStatus> findAllWithStatus();
}
