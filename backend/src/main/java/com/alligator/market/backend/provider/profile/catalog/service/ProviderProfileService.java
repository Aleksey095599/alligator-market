package com.alligator.market.backend.provider.profile.catalog.service;

import com.alligator.market.domain.provider.profile.ProviderProfile;
import com.alligator.market.domain.provider.profile.ProviderProfileStatus;
import java.util.Map;

/**
 * Контракт сервиса для работы с профилями провайдеров в хранилище данных.
 */
public interface ProviderProfileService {

    /** Вернуть все активные профили провайдеров вместе с PK. */
    Map<ProviderProfile, Long> findAllActive();

    /** Вернуть все профили провайдеров с их статусами. */
    Map<ProviderProfile, ProviderProfileStatus> findAllWithStatus();
}
