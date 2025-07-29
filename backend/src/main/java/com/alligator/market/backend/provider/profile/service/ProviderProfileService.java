package com.alligator.market.backend.provider.profile.service;

import com.alligator.market.domain.provider.profile.ProviderProfile;
import java.util.Map;

/**
 * Контракт сервиса для работы с профилями провайдеров в хранилище данных.
 */
public interface ProviderProfileService {

    /** Вернуть все активные профили провайдеров вместе с PK. */
    Map<ProviderProfile, Long> findAllActive();
}
