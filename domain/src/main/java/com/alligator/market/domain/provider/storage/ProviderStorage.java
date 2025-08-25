package com.alligator.market.domain.provider.storage;

import com.alligator.market.domain.provider.model.Provider;
import com.alligator.market.domain.provider.model.ProviderStatus;
import com.alligator.market.domain.provider.model.profile.ProviderProfile;

import java.util.Collection;
import java.util.Map;

/**
 * Репозиторий провайдеров рыночных данных.
 */
public interface ProviderStorage {

    /** Вернуть все провайдеры. */
    Map<Provider, ProviderStatus> findAllWithStatus();

    /** Вернуть все провайдеры (вместе с PK) со статусом {@link ProviderStatus#ACTIVE}. */
    Map<Long, Provider> findAllActive();

    /** Сохранить коллекцию профилей провайдеров. */
    void saveAll(Collection<ProviderProfile> profiles);

    /** Обновить статус провайдеров по их идентификаторам. */
    void updateStatus(Collection<Long> ids, ProviderStatus status);
}
