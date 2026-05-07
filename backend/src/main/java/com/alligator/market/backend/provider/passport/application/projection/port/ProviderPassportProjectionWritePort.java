package com.alligator.market.backend.provider.passport.application.projection.port;

import com.alligator.market.domain.source.passport.ProviderPassport;
import com.alligator.market.domain.source.vo.ProviderCode;
import com.alligator.market.domain.source.registry.ProviderRegistry;

import java.util.Map;
import java.util.Set;

/**
 * Write-порт проекции паспортов провайдеров в БД.
 *
 * <p>Назначение: поддерживать materialized view паспортов провайдеров в согласованном состоянии
 * относительно реестра провайдеров {@link ProviderRegistry}.</p>
 */
public interface ProviderPassportProjectionWritePort {

    /**
     * Пометить устаревшими все записи проекции, кроме указанных кодов провайдеров.
     *
     * @param currentCodes коды провайдеров из текущего реестра
     */
    void retireAllExcept(Set<ProviderCode> currentCodes);

    /**
     * Вставить или обновить паспорта провайдеров в проекции.
     *
     * <p>Семантика: для каждого {@link ProviderCode} из {@code passports} в проекции должна существовать
     * ровно одна актуальная запись, содержащая значения паспорта из входных данных.</p>
     *
     * @param passports карта "код провайдера → паспорт провайдера"
     */
    void upsertAll(Map<ProviderCode, ProviderPassport> passports);
}
