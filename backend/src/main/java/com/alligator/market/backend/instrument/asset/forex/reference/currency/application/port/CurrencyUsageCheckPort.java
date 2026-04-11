package com.alligator.market.backend.instrument.asset.forex.reference.currency.application.port;

import com.alligator.market.domain.instrument.asset.forex.reference.currency.model.vo.CurrencyCode;

/**
 * Порт проверки использования валюты во внешних агрегатах и инструментах.
 */
public interface CurrencyUsageCheckPort {

    /**
     * Проверяет, используется ли валюта хотя бы в одном внешнем агрегате или инструменте.
     */
    boolean isUsed(CurrencyCode currencyCode);
}
