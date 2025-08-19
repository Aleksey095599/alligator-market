package com.alligator.market.domain.provider.model;

import com.alligator.market.domain.instrument.model.InstrumentType;

import java.util.Set;

/**
 * Доменная модель провайдера рыночных данных.
 *
 * @param instrumentsSupported   Поддерживаемые инструменты
 * @param profileStatus          Статус профиля провайдера
 */
public record Provider (

        Set<InstrumentType>instrumentsSupported,
        ProviderProfileStatus profileStatus
) {}
