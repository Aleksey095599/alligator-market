package com.alligator.market.backend.marketdata.capture.process.passport.application.projection.port;

import com.alligator.market.domain.marketdata.capture.process.passport.MarketDataCaptureProcessPassport;
import com.alligator.market.domain.marketdata.capture.process.registry.MarketDataCaptureProcessRegistry;
import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessCode;

import java.util.Map;
import java.util.Set;

/**
 * Write-порт проекции паспортов процессов захвата рыночных данных в БД.
 *
 * <p>Назначение: поддерживать materialized view паспортов процессов захвата в согласованном состоянии
 * относительно реестра процессов захвата {@link MarketDataCaptureProcessRegistry}.</p>
 */
public interface MarketDataCaptureProcessPassportProjectionWritePort {

    /**
     * Пометить устаревшими все записи проекции, кроме указанных кодов процессов захвата.
     *
     * @param currentCodes коды процессов захвата из текущего реестра
     */
    void retireAllExcept(Set<MarketDataCaptureProcessCode> currentCodes);

    /**
     * Вставить или обновить паспорта процессов захвата в проекции.
     *
     * <p>Семантика: для каждого {@link MarketDataCaptureProcessCode} из {@code passports} в проекции должна существовать
     * ровно одна актуальная запись, содержащая значения паспорта из входных данных.</p>
     *
     * @param passports карта "код процесса захвата → паспорт процесса захвата"
     */
    void upsertAll(Map<MarketDataCaptureProcessCode, MarketDataCaptureProcessPassport> passports);
}
