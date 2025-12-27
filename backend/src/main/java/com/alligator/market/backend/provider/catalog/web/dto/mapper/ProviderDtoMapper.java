package com.alligator.market.backend.provider.catalog.web.dto.mapper;

import com.alligator.market.backend.provider.catalog.service.ProviderCatalogItem;
import com.alligator.market.backend.provider.catalog.web.dto.out.ProviderResponseDto;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;

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

        ProviderDescriptor descriptor = item.descriptor();
        Duration minUpdateInterval = item.minUpdateInterval();

        return new ProviderResponseDto(
                item.providerCode(),
                descriptor.displayName(),
                descriptor.deliveryMode().name(),
                descriptor.accessMethod().name(),
                descriptor.bulkSubscription(),
                minUpdateInterval.getSeconds()
        );
    }
}
