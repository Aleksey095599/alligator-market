package com.alligator.market.backend.source.passport.application.projection.port;

import com.alligator.market.domain.source.passport.MarketDataSourcePassport;
import com.alligator.market.domain.source.vo.MarketDataSourceCode;
import com.alligator.market.domain.source.registry.MarketDataSourceRegistry;

import java.util.Map;
import java.util.Set;

/**
 * Write port for synchronizing market data source passport projection rows.
 *
 * <p>Назначение: поддерживать materialized view паспортов провайдеров в согласованном состоянии
 * относительно реестра источников {@link MarketDataSourceRegistry}.</p>
 */
public interface MarketDataSourcePassportProjectionWritePort {

    /**
     * Пометить устаревшими все записи проекции, кроме указанных кодов провайдеров.
     *
     * @param currentCodes коды провайдеров из текущего реестра
     */
    void retireAllExcept(Set<MarketDataSourceCode> currentCodes);

    /**
     * Вставить или обновить паспорта провайдеров в проекции.
     *
     * <p>Семантика: для каждого {@link MarketDataSourceCode} из {@code passports} в проекции должна существовать
     * ровно одна актуальная запись, содержащая значения паспорта из входных данных.</p>
     *
     * @param passports карта "код провайдера → паспорт провайдера"
     */
    void upsertAll(Map<MarketDataSourceCode, MarketDataSourcePassport> passports);
}
