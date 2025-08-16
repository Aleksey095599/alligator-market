package com.alligator.market.backend.provider.profile.catalog.jpa;

import com.alligator.market.domain.provider.profile.model.ProviderProfile;

/**
 * Маппер профиля провайдера между JPA и доменной моделью.
 */
public final class ProfileEmbeddableMapper {

    private ProfileEmbeddableMapper() {
    }

    /** Преобразует доменную модель в JPA-объект. */
    public static ProfileEmbeddable toEmbeddable(ProviderProfile profile) {
        ProfileEmbeddable embeddable = new ProfileEmbeddable();
        embeddable.setProviderCode(profile.providerCode());
        embeddable.setDisplayName(profile.displayName());
        embeddable.setInstrumentTypes(profile.instrumentTypes());
        embeddable.setDeliveryMode(profile.deliveryMode());
        embeddable.setAccessMethod(profile.accessMethod());
        embeddable.setSupportsBulkSubscription(profile.supportsBulkSubscription());
        embeddable.setMinPollPeriodMs(profile.minPollPeriodMs());
        return embeddable;
    }

    /** Преобразует JPA-объект в доменную модель. */
    public static ProviderProfile toDomain(ProfileEmbeddable embeddable) {
        return new ProviderProfile(
                embeddable.getProviderCode(),
                embeddable.getDisplayName(),
                embeddable.getInstrumentTypes(),
                embeddable.getDeliveryMode(),
                embeddable.getAccessMethod(),
                embeddable.isSupportsBulkSubscription(),
                embeddable.getMinPollPeriodMs()
        );
    }
}

