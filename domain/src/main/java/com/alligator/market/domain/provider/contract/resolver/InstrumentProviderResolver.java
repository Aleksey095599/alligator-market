package com.alligator.market.domain.provider.contract.resolver;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.provider.contract.handler.AbstractInstrumentHandler;

/**
 * Контракт сервиса, который определяет соответствие провайдера и финансового инструмента.
 */
public interface InstrumentProviderResolver {

    /**
     * Определяет код провайдера рыночных данных, который должен использоваться для указанного инструмента.
     *
     * <p>Реализация может основываться на данных БД, конфигурации приложения, настройках пользователя и т.п.
     *
     * <p>Примечание: данный метод не проверяет поддержку инструмента провайдером – это делается в методе получения
     * котировки в абстрактном контракте обработчика провайдера {@link AbstractInstrumentHandler#quote(Instrument)}.
     * Цель данного метода – вернуть код провайдера, который был назначен пользователями приложения
     * для заданного инструмента.
     */
    String resolveProvider(Instrument instrument);
}
