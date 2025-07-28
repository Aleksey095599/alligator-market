package com.alligator.market.backend.provider.profile.service;

import com.alligator.market.domain.provider.profile.ProviderProfile;
import java.util.Collection;
import java.util.Map;

/**
 * Сервис для работы с профилями провайдеров в таблице БД.
 */
public interface ProviderProfileService {

    /** Вернуть все активные профили провайдеров вместе с PK */
    Map<ProviderProfile, Long> findAllActive();

    /** Сохранить коллекцию профилей со статусом ACTIVE */
    void saveAll(Collection<ProviderProfile> profiles);
}
