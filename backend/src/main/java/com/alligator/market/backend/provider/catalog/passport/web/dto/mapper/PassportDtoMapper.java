package com.alligator.market.backend.provider.catalog.passport.web.dto.mapper;

import com.alligator.market.backend.provider.catalog.passport.web.dto.out.PassportResponseDto;
import com.alligator.market.domain.provider.contract.passport.ProviderPassport;

import java.util.Objects;

/**
 * Маппер DTO для паспорта провайдера.
 */
public final class PassportDtoMapper {

    /**
     * Приватный конструктор (запрещает создание экземпляров).
     */
    private PassportDtoMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Модель --> DTO ответа.
     */
    public static PassportResponseDto toProviderPassportResponseDto(ProviderPassport providerPassport) {
        Objects.requireNonNull(providerPassport, "providerPassport must not be null");

        return new PassportResponseDto(
                providerPassport.displayName(),
                providerPassport.deliveryMode().name(),
                providerPassport.accessMethod().name(),
                providerPassport.bulkSubscription()
        );
    }
}
