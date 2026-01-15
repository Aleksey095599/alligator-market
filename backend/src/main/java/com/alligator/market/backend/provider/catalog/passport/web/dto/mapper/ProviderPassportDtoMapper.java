package com.alligator.market.backend.provider.catalog.passport.web.dto.mapper;

import com.alligator.market.backend.provider.catalog.passport.web.dto.out.ProviderPassportResponseDto;
import com.alligator.market.domain.provider.contract.passport.ProviderPassport;

import java.util.Objects;

/**
 * Маппер DTO для паспорта провайдера.
 */
public final class ProviderPassportDtoMapper {

    /**
     * Приватный конструктор (запрещает создание экземпляров).
     */
    private ProviderPassportDtoMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Модель --> DTO ответа.
     */
    public static ProviderPassportResponseDto toProviderPassportResponseDto(ProviderPassport providerPassport) {
        Objects.requireNonNull(providerPassport, "providerPassport must not be null");

        return new ProviderPassportResponseDto(
                providerPassport.displayName(),
                providerPassport.deliveryMode(),
                providerPassport.accessMethod(),
                providerPassport.bulkSubscription()
        )
    }
}
