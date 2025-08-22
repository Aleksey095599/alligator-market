package com.alligator.market.domain.provider.model;

import com.alligator.market.domain.instrument.contract.InstrumentType;
import com.alligator.market.domain.provider.profile.model.ProviderProfile;

import java.util.Set;

/**
 * Доменная модель провайдера рыночных данных.
 *
 * @param instrumentsSupported   Поддерживаемые инструменты
 * @param profileStatus          Статус профиля провайдера
 * @param providerProfile        Профиль провайдера
 */
public record Provider (

        Set<InstrumentType>instrumentsSupported,
        ProviderProfileStatus profileStatus,
        ProviderProfile providerProfile
) {}
