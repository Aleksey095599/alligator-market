package com.alligator.market.backend.provider.profile.catalog.jpa;

import com.alligator.market.backend.provider.profile.catalog.jpa.embaddable.ProfileEmbeddable;
import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import com.alligator.market.domain.provider.model.ProviderStatus;

/**
 * Маппер сущности и доменной модели профиля провайдера рыночных данных.
 */
public final class ProviderProfileEntityMapper {

    private ProviderProfileEntityMapper() {
    }

    /** Преобразует доменную модель в сущность, назначая заданный статус. */
    public static ProviderProfileEntity toEntity(ProviderProfile profile, ProviderStatus status) {
        ProviderProfileEntity entity = new ProviderProfileEntity();
        entity.setStatus(status);
        ProfileEmbeddable data = new ProfileEmbeddable();
        data.setProviderCode(profile.providerCode());
        data.setDisplayName(profile.displayName());
        data.setInstrumentTypes(profile.instrumentTypes());
        data.setDeliveryMode(profile.deliveryMode());
        data.setAccessMethod(profile.accessMethod());
        data.setSupportsBulkSubscription(profile.supportsBulkSubscription());
        data.setMinPollPeriodMs(profile.minPollPeriodMs());
        entity.setProfile(data);
        return entity;
    }

    /** Преобразует сущность в доменную модель. */
    public static ProviderProfile toDomain(ProviderProfileEntity entity) {
        ProfileEmbeddable data = entity.getProfile();
        return new ProviderProfile(
                data.getProviderCode(),
                data.getDisplayName(),
                data.getInstrumentTypes(),
                data.getDeliveryMode(),
                data.getAccessMethod(),
                data.isSupportsBulkSubscription(),
                data.getMinPollPeriodMs()
        );
    }
}
