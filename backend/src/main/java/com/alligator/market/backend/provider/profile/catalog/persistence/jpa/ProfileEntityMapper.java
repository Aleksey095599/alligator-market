package com.alligator.market.backend.provider.profile.catalog.persistence.jpa;

import com.alligator.market.domain.instrument.base.Instrument;
import com.alligator.market.domain.provider.profile.model.Profile;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Маппер: сущность профиля ⇄ доменная модель.
 */
@Component
public class ProfileEntityMapper {

    /** Преобразует сущность в доменную модель. */
    public Profile toDomain(ProfileEntity entity) {
        Set<Class<? extends Instrument>> classes = entity.getInstrumentClasses().stream()
                .map(this::classByName)
                .collect(Collectors.toSet());
        return new Profile(
                entity.getProfileStatus(),
                entity.getProviderCode(),
                entity.getDisplayName(),
                classes,
                entity.getDeliveryMode(),
                entity.getAccessMethod(),
                entity.isBulkSubscription(),
                entity.getMinPollMs()
        );
    }

    /** Преобразует доменную модель в сущность. */
    public ProfileEntity toEntity(Profile profile) {
        var entity = new ProfileEntity();
        entity.setProfileStatus(profile.profileStatus());
        entity.setProviderCode(profile.providerCode());
        entity.setDisplayName(profile.displayName());
        Set<String> classes = profile.instrumentsSupported().stream()
                .map(Class::getName)
                .collect(Collectors.toSet());
        entity.setInstrumentClasses(classes);
        entity.setDeliveryMode(profile.deliveryMode());
        entity.setAccessMethod(profile.accessMethod());
        entity.setBulkSubscription(profile.bulkSubscription());
        entity.setMinPollMs(profile.minPollMs());
        return entity;
    }

    // Получаем класс по имени
    @SuppressWarnings("unchecked")
    private Class<? extends Instrument> classByName(String name) {
        try {
            return (Class<? extends Instrument>) Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unknown instrument class: " + name, e);
        }
    }
}
