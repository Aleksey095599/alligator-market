package com.alligator.market.backend.provider.catalog.passport.web.dto.mapper;

import com.alligator.market.backend.provider.catalog.passport.service.ProviderCatalogItem;
import com.alligator.market.backend.provider.catalog.passport.web.dto.out.ProviderResponseDto;
import com.alligator.market.domain.provider.contract.passport.ProviderPassport;
import com.alligator.market.domain.provider.contract.policy.ProviderPolicy;

import java.time.Duration;
import java.util.Objects;

/**
 * Маппер DTO для провайдера рыночных данных.
 */
public final class ProviderDtoMapper {

    /**
     * Приватный конструктор (запрещает создание экземпляров).
     */
    private ProviderDtoMapper() { throw new UnsupportedOperationException("Utility class"); }

    /**
     * Преобразовать модель элемента каталога {@link ProviderCatalogItem} в DTO ответа {@link ProviderResponseDto}.
     */
    public static ProviderResponseDto toDto(ProviderCatalogItem item) {
        Objects.requireNonNull(item, "item must not be null");

        ProviderPassport passport = item.passport();
        ProviderPolicy policy = item.policy();
        Duration minUpdateInterval = policy.minUpdateInterval();

        return new ProviderResponseDto(
                item.providerCode().value(),
                passport.displayName(),
                passport.deliveryMode().name(),
                passport.accessMethod().name(),
                passport.bulkSubscription(),
                minUpdateInterval.getSeconds()
        );
    }
}
