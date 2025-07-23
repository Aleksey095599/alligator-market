package com.alligator.market.backend.provider.profile.mapper;

import com.alligator.market.backend.provider.profile.entity.ProviderProfileEntity;
import com.alligator.market.domain.provider.ProviderProfile;
import com.alligator.market.domain.provider.ProviderProfileStatus;

/**
 * Утилитарный класс для преобразования между доменной моделью
 * {@link ProviderProfile} и сущностью {@link ProviderProfileEntity}.
 */
public final class ProviderProfileMapper {

    private ProviderProfileMapper() {
    }

    /**
     * Преобразует доменную модель в сущность, назначая заданный статус.
     */
    public static ProviderProfileEntity toEntity(ProviderProfile profile, ProviderProfileStatus status) {
        ProviderProfileEntity entity = new ProviderProfileEntity();
        entity.setStatus(status);
        entity.setProviderCode(profile.providerCode());
        entity.setDisplayName(profile.displayName());
        entity.setInstrumentTypes(profile.instrumentTypes());
        entity.setDeliveryMode(profile.deliveryMode());
        entity.setAccessMethod(profile.accessMethod());
        entity.setSupportsBulkSubscription(profile.supportsBulkSubscription());
        entity.setMinPollPeriodMs(profile.minPollPeriodMs());
        return entity;
    }

    /**
     * Преобразует сущность в доменную модель, игнорируя статус.
     */
    public static ProviderProfile toDomain(ProviderProfileEntity entity) {
        return new ProviderProfile(
                entity.getProviderCode(),
                entity.getDisplayName(),
                entity.getInstrumentTypes(),
                entity.getDeliveryMode(),
                entity.getAccessMethod(),
                entity.isSupportsBulkSubscription(),
                entity.getMinPollPeriodMs()
        );
    }
}
