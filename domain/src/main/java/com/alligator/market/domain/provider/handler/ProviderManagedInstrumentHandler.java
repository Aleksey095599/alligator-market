package com.alligator.market.domain.provider.handler;

import com.alligator.market.domain.instrument.base.Instrument;
import com.alligator.market.domain.instrument.base.vo.InstrumentCode;
import com.alligator.market.domain.provider.MarketDataProvider;
import com.alligator.market.domain.provider.vo.HandlerCode;
import com.alligator.market.domain.marketdata.tick.model.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.Set;

/**
 * Внутренний SPI обработчика инструмента, управляемого провайдером.
 *
 * <p>Примечание: это provider-facing контракт, а не внешний доменный API.</p>
 */
public interface ProviderManagedInstrumentHandler<P extends MarketDataProvider, I extends Instrument> {

    /**
     * Уникальный код обработчика (идентификатор).
     */
    HandlerCode handlerCode();

    /**
     * Коды поддерживаемых инструментов.
     */
    Set<InstrumentCode> supportedInstrumentCodes();

    /**
     * Прикрепляет обработчик к провайдеру.
     */
    void attachTo(P provider);

    /**
     * Поток котировок для заданного инструмента.
     */
    Publisher<QuoteTick> quote(I instrument);
}
