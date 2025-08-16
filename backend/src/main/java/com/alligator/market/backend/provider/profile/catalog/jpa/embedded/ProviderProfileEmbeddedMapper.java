package com.alligator.market.backend.provider.profile.catalog.jpa.embedded;

import com.alligator.market.domain.provider.profile.model.ProviderProfile;

/**
 * Маппер доменной модели и встраиваемого профиля.
 */
public final class ProviderProfileEmbeddedMapper {

    private ProviderProfileEmbeddedMapper() {
    }

    /** Преобразует доменную модель во встраиваемый профиль. */
    public static ProviderProfileEmbedded toEmbedded(ProviderProfile profile) {
        ProviderProfileEmbedded embedded = new ProviderProfileEmbedded();
        embedded.setProviderCode(profile.providerCode());
        embedded.setDisplayName(profile.displayName());
        embedded.setInstrumentTypes(profile.instrumentTypes());
        embedded.setDeliveryMode(profile.deliveryMode());
        embedded.setAccessMethod(profile.accessMethod());
        embedded.setBulkSubscription(profile.bulkSubscription());
        embedded.setMinPollMs(profile.minPollMs());
        return embedded;
    }

    /** Преобразует встраиваемый профиль в доменную модель. */
    public static ProviderProfile toDomain(ProviderProfileEmbedded embedded) {
        return new ProviderProfile(
                embedded.getProviderCode(),
                embedded.getDisplayName(),
                embedded.getInstrumentTypes(),
                embedded.getDeliveryMode(),
                embedded.getAccessMethod(),
                embedded.isBulkSubscription(),
                embedded.getMinPollMs()
        );
    }
}
