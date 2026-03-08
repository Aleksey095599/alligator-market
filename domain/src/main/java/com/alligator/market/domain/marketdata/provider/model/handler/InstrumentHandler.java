package com.alligator.market.domain.marketdata.provider.model.handler;

import com.alligator.market.domain.instrument.base.model.Instrument;
import com.alligator.market.domain.instrument.base.model.classification.AssetClass;
import com.alligator.market.domain.instrument.base.model.classification.ContractType;
import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.provider.model.MarketDataProvider;
import com.alligator.market.domain.marketdata.provider.model.vo.HandlerCode;
import com.alligator.market.domain.quote.response.tick.model.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.Set;

/**
 * Обработчик (handler) финансового инструмента {@link Instrument} для заданного провайдера {@link MarketDataProvider}.
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
