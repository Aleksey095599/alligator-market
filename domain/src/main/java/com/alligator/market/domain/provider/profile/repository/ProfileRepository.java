package com.alligator.market.domain.provider.profile.repository;

import com.alligator.market.domain.provider.profile.model.ProfileStatus;
import com.alligator.market.domain.provider.profile.model.Profile;

import java.util.Collection;
import java.util.Map;

/**
 * Репозиторий профилей провайдеров.
 */
public interface ProfileRepository {

    /** Вернуть все провайдеры (вместе с PK) со статусом {@link ProfileStatus#ACTIVE}. */
    Map<Long, Profile> findAllActive();

    /** Сохранить коллекцию профилей провайдеров. */
    void saveAll(Collection<Profile> profiles);

    /** Обновить статус провайдеров по их идентификаторам. */
    void updateStatus(Collection<Long> ids, ProfileStatus status);
}
