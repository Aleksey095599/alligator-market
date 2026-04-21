package com.alligator.market.domain.provider.handler;

import com.alligator.market.domain.instrument.base.Instrument;
import com.alligator.market.domain.instrument.base.classification.AssetClass;
import com.alligator.market.domain.instrument.base.classification.ContractType;
import com.alligator.market.domain.instrument.base.vo.InstrumentCode;
import com.alligator.market.domain.provider.MarketDataProvider;
import com.alligator.market.domain.provider.vo.HandlerCode;
import com.alligator.market.domain.marketdata.tick.model.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.Set;

/**
 * Доменный контракт обработчика финансового инструмента.
 */
public interface InstrumentHandler<P extends MarketDataProvider, I extends Instrument> {

    /**
     * Уникальный код обработчика (идентификатор).
     */
    HandlerCode handlerCode();

    /**
     * Класс актива поддерживаемых инструментов.
     */
    AssetClass assetClass();

    /**
     * Тип контракта поддерживаемых инструментов.
     */
    ContractType contractType();

    /**
     * Коды поддерживаемых инструментов.
     */
    Set<InstrumentCode> supportedInstrumentCodes();

    /**
     * Поток котировок для заданного инструмента.
     */
    Publisher<QuoteTick> quote(I instrument);
}
