package com.alligator.market.backend.provider.profile.service;

import com.alligator.market.backend.provider.profile.entity.ProviderProfileEntity;
import com.alligator.market.domain.provider.profile.ProviderProfile;
import java.util.Collection;
import java.util.Map;

/**
 * Сервис для работы с профилями провайдеров в таблице БД.
 */
public interface ProviderProfileService {

    /** Вернуть все профили провайдеров вместе с PK */
    Map<ProviderProfile, Long> findAll();

    /** Сохранить коллекцию профилей */
    void saveAll(Collection<ProviderProfileEntity> entities);
}
