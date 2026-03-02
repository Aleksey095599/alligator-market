package com.alligator.market.domain.quote.feed;

import com.alligator.market.domain.instrument.base.Instrument;
import com.alligator.market.domain.provider.model.vo.ProviderCode;

/**
 * Сервис для решения соответствия: «финансовый инструмент → провайдер рыночных данных».
 */
public interface InstrumentProviderResolver {

    /**
     * Возвращает код провайдера, назначенного для переданного инструмента.</p>
     */
    ProviderCode resolveProvider(Instrument instrument);
}
