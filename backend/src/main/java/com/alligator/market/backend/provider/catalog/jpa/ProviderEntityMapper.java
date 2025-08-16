package com.alligator.market.backend.provider.catalog.jpa;

import com.alligator.market.backend.provider.profile.catalog.jpa.ProfileEmbeddable;
import com.alligator.market.backend.provider.profile.catalog.jpa.ProfileEmbeddableMapper;
import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import com.alligator.market.domain.provider.model.ProviderStatus;

/**
 * Маппер сущности и доменной модели профиля провайдера рыночных данных.
 */
public final class ProviderEntityMapper {

    private ProviderEntityMapper() {
    }

    /** Преобразует доменную модель в сущность, назначая заданный статус. */
    public static ProviderEntity toEntity(ProviderProfile profile, ProviderStatus status) {
        ProviderEntity entity = new ProviderEntity();
        entity.setStatus(status);
        ProfileEmbeddable data = ProfileEmbeddableMapper.toEmbeddable(profile);
        entity.setProfile(data);
        return entity;
    }

    /** Преобразует сущность в доменную модель. */
    public static ProviderProfile toDomain(ProviderEntity entity) {
        ProfileEmbeddable data = entity.getProfile();
        return ProfileEmbeddableMapper.toDomain(data);
    }
}
