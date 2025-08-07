package com.alligator.market.domain.provider.profile.catalog;

import com.alligator.market.domain.provider.profile.ProviderProfile;
import com.alligator.market.domain.provider.profile.ProviderProfileStatus;

import java.util.Collection;
import java.util.Map;

/**
 * Контракт хранилища профилей провайдеров.
 */
public interface ProviderProfileStorage {

    /** Вернуть все активные профили провайдеров вместе с PK. */
    Map<ProviderProfile, Long> findAllActive();

    /** Вернуть все профили провайдеров с их статусами. */
    Map<ProviderProfile, ProviderProfileStatus> findAllWithStatus();

    /** Сохранить коллекцию профилей. */
    void saveAll(Collection<ProviderProfile> profiles);

    /** Обновить статус профилей по их идентификаторам. */
    void updateStatus(Collection<Long> ids, ProviderProfileStatus status);
}
