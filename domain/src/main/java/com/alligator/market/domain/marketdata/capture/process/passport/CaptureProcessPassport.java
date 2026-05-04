package com.alligator.market.domain.marketdata.capture.process.passport;

import com.alligator.market.domain.marketdata.capture.process.vo.CaptureProcessDisplayName;

import java.util.Objects;

/**
 * Паспорт процесса фиксации рыночных данных: иммутабельные метаданные для витрины.
 *
 * <p>Назначение: отображение в каталоге, UI/API, логах и диагностике.</p>
 *
 * @param displayName отображаемое имя процесса фиксации
 */
public record CaptureProcessPassport(
        CaptureProcessDisplayName displayName
) {

    public CaptureProcessPassport {
        Objects.requireNonNull(displayName, "displayName must not be null");
    }
}
