package com.alligator.market.domain.provider.model;

import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.provider.model.profile.ProviderProfile;

import java.util.Set;

/**
 * Доменная модель провайдера рыночных данных.
 *
 * @param instrumentsSupported   Поддерживаемые инструменты {@link InstrumentType}
 * @param providerStatus         Статус провайдера {@link ProviderStatus}
 * @param providerProfile        Профиль провайдера {@link ProviderProfile}
 */
public record Provider (

        ProviderStatus providerStatus,
        Set<InstrumentType>instrumentsSupported,
        ProviderProfile providerProfile
) {}
