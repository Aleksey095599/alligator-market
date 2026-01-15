package com.alligator.market.backend.provider.catalog.passport.web.dto.mapper;

import com.alligator.market.backend.provider.catalog.passport.web.dto.out.ProviderPassportResponseDto;
import com.alligator.market.domain.provider.contract.passport.ProviderPassport;

import java.util.Objects;

/**
 * Маппер DTO паспорта провайдера и доменной модели провайдера.
 */
public final class ProviderPassportDtoMapper {

    /**
     * Приватный конструктор (запрещает создание экземпляров).
     */
    private ProviderPassportDtoMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Преобразует доменную модель {@link ProviderPassport} в DTO ответа {@link ProviderPassportResponseDto}.
     */
    public static ProviderPassportResponseDto toDto(ProviderPassport passport) {
        Objects.requireNonNull(passport, "passport must not be null");

        return new ProviderPassportResponseDto(
                passport.displayName(),
                passport.deliveryMode().name(),
                passport.accessMethod().name(),
                passport.bulkSubscription()
        );
    }
}
