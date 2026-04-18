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
     * Класс поддерживаемых инструментов.
     */
    Class<I> instrumentClass();

    /**
     * Класс актива поддерживаемых инструментов.
     */
    AssetClass assetClass();

    /**
     * Тип контракта поддерживаемых инструментов.
     */
    ContractType contractType();

    /**
     * Признак: инструмент сопоставим с обработчиком по доменным признакам (java-класс + класс актива + тип контракта).
     */
    default boolean isCompatible(Instrument instrument) {
        return instrument != null
                && instrumentClass().isInstance(instrument)
                && instrument.assetClass() == assetClass()
                && instrument.contractType() == contractType();
    }

    /**
     * Коды поддерживаемых инструментов.
     */
    Set<InstrumentCode> supportedInstrumentCodes();

    /**
     * Признак: поддерживается ли конкретный код инструмента.
     */
    default boolean isSupported(InstrumentCode instrumentCode) {
        return instrumentCode != null && supportedInstrumentCodes().contains(instrumentCode);
    }

    /**
     * Прикрепление обработчика к провайдеру.
     */
    void attachTo(P provider);

    /**
     * Признак: обработчик уже прикреплён к провайдеру.
     *
     * <p>Назначение: Одно из требований корректного состояния обработчика – прикрепление к провайдеру.
     * Перед выполнением метода получения потока котировок, обработчик проверяет прикрепление к провайдеру.</p>
     */
    boolean isAttached();

    /**
     * Поток котировок для заданного инструмента.
     */
    Publisher<QuoteTick> quote(I instrument);
}
