package com.alligator.market.domain.provider.profile.repository;

import com.alligator.market.domain.provider.profile.model.ProviderProfileStatus;
import com.alligator.market.domain.provider.profile.model.ProviderProfile;

import java.util.Collection;
import java.util.Map;

/**
 * Репозиторий провайдеров рыночных данных.
 */
public interface ProviderProfileRepository {

    /** Вернуть все провайдеры. */
    Map<Provider, ProviderProfileStatus> findAllWithStatus();

    /** Вернуть все провайдеры (вместе с PK) со статусом {@link ProviderProfileStatus#ACTIVE}. */
    Map<Long, Provider> findAllActive();

    /** Сохранить коллекцию профилей провайдеров. */
    void saveAll(Collection<ProviderProfile> profiles);

    /** Обновить статус провайдеров по их идентификаторам. */
    void updateStatus(Collection<Long> ids, ProviderProfileStatus status);
}
