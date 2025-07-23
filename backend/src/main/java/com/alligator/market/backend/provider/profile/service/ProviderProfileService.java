package com.alligator.market.backend.provider.profile.service;

import com.alligator.market.backend.provider.profile.entity.ProviderProfileEntity;
import java.util.Collection;
import java.util.List;

/**
 * Сервис для работы с профилями провайдеров в таблице БД.
 */
public interface ProviderProfileService {

    /** Вернуть все профили провайдеров */
    List<ProviderProfileEntity> findAll();

    /** Сохранить коллекцию профилей */
    void saveAll(Collection<ProviderProfileEntity> entities);
}
