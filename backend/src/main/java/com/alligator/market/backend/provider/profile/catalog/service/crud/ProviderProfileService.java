package com.alligator.market.backend.provider.profile.catalog.service.crud;

import com.alligator.market.domain.provider.profile.model.Profile;
import com.alligator.market.domain.provider.profile.model.ProfileStatus;
import java.util.Map;

/**
 * Сервис профилей провайдеров рыночных данных.
 */
public interface ProviderProfileService {

    /** Вернуть все активные профили вместе с PK. */
    Map<Long, Profile> findAllActive();

    /** Вернуть все профили с их статусами. */
    Map<Profile, ProfileStatus> findAllWithStatus();
}
