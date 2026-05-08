package com.alligator.market.backend.sourceplan.plan.application.exception;

import com.alligator.market.domain.marketdata.capturer.vo.MarketDataCapturerCode;

import java.util.Objects;

/**
 * Ошибка application-слоя: процесс захвата с указанным кодом не найден.
 */
public final class MarketDataCapturerCodeNotFoundException extends IllegalArgumentException {

    public MarketDataCapturerCodeNotFoundException(MarketDataCapturerCode capturerCode) {
        super("Capturer code '" + Objects.requireNonNull(capturerCode,
                "capturerCode must not be null").value() + "' does not exist");
    }
}
