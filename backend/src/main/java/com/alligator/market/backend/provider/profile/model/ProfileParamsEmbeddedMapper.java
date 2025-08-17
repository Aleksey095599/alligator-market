package com.alligator.market.backend.provider.profile.model;

import com.alligator.market.domain.provider.profile.model.ProviderProfile;

/**
 * Маппер встраиваемого компонента профиля провайдера {@link ProfileParamsEmbedded} и
 * доменной модели профиля провайдера рыночных данных {@link ProviderProfile}.
 */
public final class ProfileParamsEmbeddedMapper {

    private ProfileParamsEmbeddedMapper() {
    }

    /** Преобразует доменную модель во встраиваемый компонент. */
    public static ProfileParamsEmbedded toEmbedded(ProviderProfile profile) {
        ProfileParamsEmbedded embedded = new ProfileParamsEmbedded();
        embedded.setProviderCode(profile.providerCode());
        embedded.setDisplayName(profile.displayName());
        embedded.setInstrumentTypes(profile.instrumentTypes());
        embedded.setDeliveryMode(profile.deliveryMode());
        embedded.setAccessMethod(profile.accessMethod());
        embedded.setBulkSubscription(profile.bulkSubscription());
        embedded.setMinPollMs(profile.minPollMs());
        return embedded;
    }

    /** Преобразует встраиваемый компонент в доменную модель. */
    public static ProviderProfile toDomain(ProfileParamsEmbedded embedded) {
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
