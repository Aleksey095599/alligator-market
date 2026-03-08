package com.alligator.market.backend.provider.catalog.passport.web.dto.mapper;

import com.alligator.market.backend.provider.catalog.passport.web.dto.out.PassportResponseDto;
import com.alligator.market.domain.marketdata.provider.model.passport.ProviderPassport;
import com.alligator.market.domain.marketdata.provider.model.vo.ProviderCode;

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
    public static PassportResponseDto toProviderPassportResponseDto(ProviderCode providerCode,
                                                                    ProviderPassport providerPassport) {
        Objects.requireNonNull(providerCode, "providerCode must not be null");
        Objects.requireNonNull(providerPassport, "providerPassport must not be null");

        return new PassportResponseDto(
                providerCode.value(),
                providerPassport.displayName(),
                providerPassport.deliveryMode().name(),
                providerPassport.accessMethod().name(),
                providerPassport.bulkSubscription()
        );
    }
}
