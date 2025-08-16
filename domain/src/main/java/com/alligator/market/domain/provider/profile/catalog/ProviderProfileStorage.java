package com.alligator.market.domain.provider.profile.catalog;

import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import com.alligator.market.domain.provider.profile.context.ProviderProfileStatus;

import java.util.Collection;
import java.util.Map;

/**
 * Хранилище профилей провайдеров рыночных данных.
 */
public interface ProviderProfileStorage {

    /** Вернуть все активные профили вместе с PK. */
    Map<Long, ProviderProfile> findAllActive();

    /** Вернуть все профили с их статусами. */
    Map<ProviderProfile, ProviderProfileStatus> findAllWithStatus();

    /** Сохранить коллекцию профилей. */
    void saveAll(Collection<ProviderProfile> profiles);

    /** Обновить статус профилей по их идентификаторам. */
    void updateStatus(Collection<Long> ids, ProviderProfileStatus status);
}
