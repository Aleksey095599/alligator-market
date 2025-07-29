package com.alligator.market.domain.provider.profile;

import java.util.Collection;
import java.util.Map;


/**
 * Порт хранилища профилей провайдеров.
 */
public interface ProviderProfileStorage {

    /** Вернуть все активные профили провайдеров вместе с PK. */
    Map<ProviderProfile, Long> findAllActive();

    /** Сохранить коллекцию профилей со статусом ACTIVE. */
    void saveAll(Collection<ProviderProfile> profiles);

    /** Обновить статус профилей по их идентификаторам. */
    void updateStatus(Collection<Long> ids, ProviderProfileStatus status);
}
