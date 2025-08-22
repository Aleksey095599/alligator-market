package com.alligator.market.backend.provider.profile.catalog.service.crud;

import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import com.alligator.market.domain.provider.model.ProviderStatus;
import java.util.Map;

/**
 * Сервис профилей провайдеров рыночных данных.
 */
public interface ProviderProfileService {

    /** Вернуть все активные профили вместе с PK. */
    Map<Long, ProviderProfile> findAllActive();

    /** Вернуть все профили с их статусами. */
    Map<ProviderProfile, ProviderStatus> findAllWithStatus();
}
