package com.alligator.market.domain.marketdata.capture.process.passport;

import com.alligator.market.domain.marketdata.capture.process.vo.MDCaptureProcessDisplayName;

import java.util.Objects;

/**
 * Паспорт процесса фиксации рыночных данных: иммутабельные метаданные для витрины.
 *
 * <p>Назначение: отображение в каталоге, UI/API, логах и диагностике.</p>
 *
 * @param displayName отображаемое имя процесса фиксации
 */
public record MDCaptureProcessPassport(
        MDCaptureProcessDisplayName displayName
) {

    public MDCaptureProcessPassport {
        Objects.requireNonNull(displayName, "displayName must not be null");
    }
}
