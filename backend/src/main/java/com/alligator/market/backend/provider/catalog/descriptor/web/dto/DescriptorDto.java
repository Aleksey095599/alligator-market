package com.alligator.market.backend.provider.catalog.descriptor.web.dto;

import com.alligator.market.domain.provider.contract.descriptor.AccessMethod;
import com.alligator.market.domain.provider.contract.descriptor.DeliveryMode;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Основной DTO для дескриптора провайдера {@link ProviderDescriptor}.
 *
 * @param providerCode Технический код провайдера.
 */
public record DescriptorDto(
        @NotBlank String providerCode,
        @NotBlank String displayName,
        @NotNull DeliveryMode deliveryMode,
        @NotNull AccessMethod accessMethod,
        boolean bulkSubscription
) {}
