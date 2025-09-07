package com.alligator.market.backend.provider.profile.catalog.api.dto;

import com.alligator.market.backend.provider.profile.catalog.api.ProviderProfileStatusDto;
import com.alligator.market.domain.instrument.base.Instrument;
import com.alligator.market.domain.provider.profile.model.Profile;
import com.alligator.market.domain.provider.profile.model.ProfileStatus;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Маппер: профиль провайдера ⇄ DTO.
 */
@Component
public class ProfileDtoMapper {

    /** Преобразует доменную модель в основной DTO. */
    public ProfileDto toDto(Profile profile) {
        Set<String> classes = profile.instrumentsSupported().stream()
                .map(Class::getName)
                .collect(Collectors.toSet());
        return new ProfileDto(
                profile.profileStatus(),
                profile.providerCode(),
                profile.displayName(),
                classes,
                profile.deliveryMode(),
                profile.accessMethod(),
                profile.bulkSubscription(),
                profile.minPollMs()
        );
    }

    /** Преобразует основной DTO в доменную модель. */
    public Profile toDomain(ProfileDto dto) {
        Set<Class<? extends Instrument>> classes = dto.instrumentsSupported().stream()
                .map(this::classByName)
                .collect(Collectors.toSet());
        return new Profile(
                dto.profileStatus(),
                dto.providerCode(),
                dto.displayName(),
                classes,
                dto.deliveryMode(),
                dto.accessMethod(),
                dto.bulkSubscription(),
                dto.minPollMs()
        );
    }

    /** Преобразует доменную модель и статус в DTO для аудита. */
    public ProviderProfileStatusDto toStatusDto(Profile profile, ProfileStatus status) {
        Set<String> classes = profile.instrumentsSupported().stream()
                .map(Class::getName)
                .collect(Collectors.toSet());
        return new ProviderProfileStatusDto(
                profile.providerCode(),
                profile.displayName(),
                classes,
                profile.deliveryMode(),
                profile.accessMethod(),
                profile.bulkSubscription(),
                profile.minPollMs(),
                status
        );
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
