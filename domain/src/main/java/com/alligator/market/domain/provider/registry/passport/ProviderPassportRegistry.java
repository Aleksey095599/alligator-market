package com.alligator.market.domain.provider.registry.passport;

import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.provider.model.passport.ProviderPassport;

import java.util.Map;

/**
 * Реестр паспортов провайдеров рыночных данных.
 */
public sealed interface ProviderPassportRegistry permits AbstractProviderPassportRegistry {

    /**
     * Возвращает карту "код провайдера → паспорт провайдера".
     */
    Map<ProviderCode, ProviderPassport> passportsByCode();
}
