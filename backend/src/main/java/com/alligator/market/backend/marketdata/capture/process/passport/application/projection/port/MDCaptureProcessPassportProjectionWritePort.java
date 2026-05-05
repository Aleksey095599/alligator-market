package com.alligator.market.backend.marketdata.capture.process.passport.application.projection.port;

import com.alligator.market.domain.marketdata.capture.process.passport.MDCaptureProcessPassport;
import com.alligator.market.domain.marketdata.capture.process.registry.MDCaptureProcessRegistry;
import com.alligator.market.domain.marketdata.capture.process.vo.MDCaptureProcessCode;

import java.util.Map;
import java.util.Set;

/**
 * Write-порт проекции паспортов процессов фиксации рыночных данных в БД.
 *
 * <p>Назначение: поддерживать materialized view паспортов процессов фиксации в согласованном состоянии
 * относительно реестра процессов фиксации {@link MDCaptureProcessRegistry}.</p>
 */
public interface MDCaptureProcessPassportProjectionWritePort {

    /**
     * Пометить устаревшими все записи проекции, кроме указанных кодов процессов фиксации.
     *
     * @param currentCodes коды процессов фиксации из текущего реестра
     */
    void retireAllExcept(Set<MDCaptureProcessCode> currentCodes);

    /**
     * Вставить или обновить паспорта процессов фиксации в проекции.
     *
     * <p>Семантика: для каждого {@link MDCaptureProcessCode} из {@code passports} в проекции должна существовать
     * ровно одна актуальная запись, содержащая значения паспорта из входных данных.</p>
     *
     * @param passports карта "код процесса фиксации → паспорт процесса фиксации"
     */
    void upsertAll(Map<MDCaptureProcessCode, MDCaptureProcessPassport> passports);
}
