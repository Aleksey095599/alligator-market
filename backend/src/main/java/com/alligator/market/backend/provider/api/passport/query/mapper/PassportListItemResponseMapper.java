package com.alligator.market.backend.provider.api.passport.query.mapper;

import com.alligator.market.backend.provider.api.passport.query.dto.PassportListItemResponse;
import com.alligator.market.domain.provider.passport.ProviderPassport;
import com.alligator.market.domain.provider.vo.ProviderCode;

import java.util.Objects;

/**
 * Маппер DTO для паспорта провайдера.
 */
public final class PassportListItemResponseMapper {

    /**
     * Приватный конструктор (запрещает создание экземпляров).
     */
    private PassportListItemResponseMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Модель --> DTO ответа.
     */
    public static PassportListItemResponse toProviderPassportResponseDto(ProviderCode providerCode,
                                                                         ProviderPassport providerPassport) {
        Objects.requireNonNull(providerCode, "providerCode must not be null");
        Objects.requireNonNull(providerPassport, "providerPassport must not be null");

        return new PassportListItemResponse(
                providerCode.value(),
                providerPassport.displayName(),
                providerPassport.deliveryMode().name(),
                providerPassport.accessMethod().name(),
                providerPassport.bulkSubscription()
        );
    }
}
