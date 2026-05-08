package com.alligator.market.backend.marketdata.capturer.passport.application.projection.port;

import com.alligator.market.domain.marketdata.capturer.passport.MarketDataCapturerPassport;
import com.alligator.market.domain.marketdata.capturer.registry.MarketDataCapturerRegistry;
import com.alligator.market.domain.marketdata.capturer.vo.MarketDataCapturerCode;

import java.util.Map;
import java.util.Set;

/**
 * Write-порт проекции паспортов процессов захвата рыночных данных в БД.
 *
 * <p>Назначение: поддерживать materialized view паспортов процессов захвата в согласованном состоянии
 * относительно реестра процессов захвата {@link MarketDataCapturerRegistry}.</p>
 */
public interface MarketDataCapturerPassportProjectionWritePort {

    /**
     * Пометить устаревшими все записи проекции, кроме указанных кодов процессов захвата.
     *
     * @param currentCodes коды процессов захвата из текущего реестра
     */
    void retireAllExcept(Set<MarketDataCapturerCode> currentCodes);

    /**
     * Вставить или обновить паспорта процессов захвата в проекции.
     *
     * <p>Семантика: для каждого {@link MarketDataCapturerCode} из {@code passports} в проекции должна существовать
     * ровно одна актуальная запись, содержащая значения паспорта из входных данных.</p>
     *
     * @param passports карта "код процесса захвата → паспорт процесса захвата"
     */
    void upsertAll(Map<MarketDataCapturerCode, MarketDataCapturerPassport> passports);
}
