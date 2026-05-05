package com.alligator.market.domain.marketdata.capture.process.passport;

import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessDisplayName;

import java.util.Objects;

/**
 * Паспорт процесса захвата рыночных данных: иммутабельные метаданные для витрины.
 *
 * <p>Назначение: отображение в каталоге, UI/API, логах и диагностике.</p>
 *
 * @param displayName отображаемое имя процесса
 */
public record MarketDataCaptureProcessPassport(
        MarketDataCaptureProcessDisplayName displayName
) {

    public MarketDataCaptureProcessPassport {
        Objects.requireNonNull(displayName, "displayName must not be null");
    }
}
