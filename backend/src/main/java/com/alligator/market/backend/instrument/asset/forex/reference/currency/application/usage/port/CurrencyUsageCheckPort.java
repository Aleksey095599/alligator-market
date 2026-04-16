package com.alligator.market.backend.instrument.asset.forex.reference.currency.application.usage.port;

import com.alligator.market.domain.instrument.asset.forex.reference.currency.code.CurrencyCode;

/**
 * Порт проверки использования валюты в приложении.
 */
public interface CurrencyUsageCheckPort {

    /**
     * Флаг, сигнализирующий об использовании валюты.
     */
    boolean isUsed(CurrencyCode currencyCode);
}
