package com.alligator.market.domain.quote.feed;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.provider.code.ProviderCode;

/**
 * Контракт сервиса, который разрешает соответствие: «финансовый инструмент → провайдер рыночных данных».
 */
public interface InstrumentProviderResolver {

    /**
     * Возвращает код провайдера рыночных данных, назначенного для указанного инструмента.
     *
     * <p>Реализация может основываться на данных БД, конфигурации приложения, настройках пользователя и т.п.</p>
     *
     * <p>Примечание: данный метод не проверяет факт поддержки инструмента провайдером – это делается в контрактах
     * провайдера и обработчика. Цель данного метода – вернуть код провайдера, назначенного для получения
     * потока котировок для заданного инструмента.</p>
     */
    ProviderCode resolveProvider(Instrument instrument);
}
