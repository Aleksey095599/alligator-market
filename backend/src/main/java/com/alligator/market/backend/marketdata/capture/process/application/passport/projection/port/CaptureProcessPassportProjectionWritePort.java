package com.alligator.market.backend.marketdata.capture.process.application.passport.projection.port;

import com.alligator.market.domain.marketdata.capture.process.passport.CaptureProcessPassport;
import com.alligator.market.domain.marketdata.capture.process.registry.CaptureProcessRegistry;
import com.alligator.market.domain.marketdata.capture.process.vo.CaptureProcessCode;

import java.util.Map;
import java.util.Set;

/**
 * Write-порт проекции паспортов процессов фиксации рыночных данных в БД.
 *
 * <p>Назначение: поддерживать materialized view паспортов процессов фиксации в согласованном состоянии
 * относительно реестра процессов фиксации {@link CaptureProcessRegistry}.</p>
 */
public interface CaptureProcessPassportProjectionWritePort {

    /**
     * Удалить все записи проекции, кроме указанных кодов процессов фиксации.
     *
     * @param activeCodes коды активных процессов фиксации
     */
    void deleteAllExcept(Set<CaptureProcessCode> activeCodes);

    /**
     * Вставить или обновить паспорта процессов фиксации в проекции.
     *
     * <p>Семантика: для каждого {@link CaptureProcessCode} из {@code passports} в проекции должна существовать
     * ровно одна актуальная запись, содержащая значения паспорта из входных данных.</p>
     *
     * @param passports карта "код процесса фиксации → паспорт процесса фиксации"
     */
    void upsertAll(Map<CaptureProcessCode, CaptureProcessPassport> passports);
}
