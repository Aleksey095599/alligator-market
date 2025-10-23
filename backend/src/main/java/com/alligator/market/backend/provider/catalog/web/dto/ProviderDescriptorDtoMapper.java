package com.alligator.market.backend.provider.catalog.web.dto;

import com.alligator.market.backend.provider.catalog.service.ProviderCatalogItem;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;

import java.time.Duration;
import java.util.Objects;

/**
 * Маппер доменных моделей каталога провайдеров в DTO.
 */
public final class ProviderDescriptorDtoMapper {

    private ProviderDescriptorDtoMapper() {
    }

    /** Преобразовать элемент каталога в DTO. */
    public static ProviderDescriptorDto toDto(ProviderCatalogItem item) {
        Objects.requireNonNull(item, "item must not be null");

        ProviderDescriptor descriptor = item.descriptor();
        Duration minUpdateInterval = item.minUpdateInterval();

        return new ProviderDescriptorDto(
                item.providerCode(),
                descriptor.displayName(),
                descriptor.deliveryMode().name(),
                descriptor.accessMethod().name(),
                descriptor.bulkSubscription(),
                minUpdateInterval.getSeconds()
        );
    }
}
