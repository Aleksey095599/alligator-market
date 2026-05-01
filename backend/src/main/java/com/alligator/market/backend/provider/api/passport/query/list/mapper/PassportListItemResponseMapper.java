package com.alligator.market.backend.provider.api.passport.query.list.mapper;

import com.alligator.market.backend.provider.api.passport.query.list.dto.PassportListItemResponse;
import com.alligator.market.domain.provider.passport.ProviderPassport;
import com.alligator.market.domain.provider.vo.ProviderCode;

import java.util.Objects;

/**
 * Маппер паспорта провайдера и {@link PassportListItemResponse}.
 */
public final class PassportListItemResponseMapper {

    private PassportListItemResponseMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static PassportListItemResponse toResponse(ProviderCode providerCode,
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
