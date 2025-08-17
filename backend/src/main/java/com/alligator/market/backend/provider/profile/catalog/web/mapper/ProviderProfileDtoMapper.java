package com.alligator.market.backend.provider.profile.catalog.web.mapper;

import com.alligator.market.backend.provider.profile.catalog.web.dto.ProviderProfileDto;
import com.alligator.market.backend.provider.profile.catalog.web.dto.ProviderProfileStatusDto;
import com.alligator.market.domain.provider.profile.context.ProviderProfileStatus;
import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Маппер: профиль провайдера ⇄ DTO.
 */
@Mapper(componentModel = "spring")
public interface ProviderProfileDtoMapper {

    /** Преобразует доменную модель в DTO. */
    @Mapping(target = "providerDeliveryMode", source = "deliveryMode")
    @Mapping(target = "providerAccessMethod", source = "accessMethod")
    @Mapping(target = "supportsBulkSubscription", source = "bulkSubscription")
    @Mapping(target = "minPollPeriodMs", source = "minPollMs")
    ProviderProfileDto toDto(ProviderProfile profile);

    /** Преобразует доменную модель и статус в DTO. */
    @Mapping(target = "status", source = "status")
    @Mapping(target = "providerDeliveryMode", source = "profile.deliveryMode")
    @Mapping(target = "providerAccessMethod", source = "profile.accessMethod")
    @Mapping(target = "supportsBulkSubscription", source = "profile.bulkSubscription")
    @Mapping(target = "minPollPeriodMs", source = "profile.minPollMs")
    @Mapping(target = "providerCode", source = "profile.providerCode")
    @Mapping(target = "displayName", source = "profile.displayName")
    @Mapping(target = "instrumentTypes", source = "profile.instrumentTypes")
    ProviderProfileStatusDto toStatusDto(ProviderProfile profile, ProviderProfileStatus status);
}
