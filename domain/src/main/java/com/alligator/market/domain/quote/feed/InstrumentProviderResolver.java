package com.alligator.market.domain.quote.feed;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.provider.code.ProviderCode;

/**
 * Сервис для решения соответствия: «финансовый инструмент → провайдер рыночных данных».
 */
public interface InstrumentProviderResolver {

    /**
     * Возвращает код провайдера, назначенного для переданного инструмента.</p>
     */
    ProviderCode resolveProvider(Instrument instrument);
}
