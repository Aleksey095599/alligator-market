package com.alligator.market.domain.provider.maintenance.projection.db.passport.service;

import com.alligator.market.domain.provider.model.passport.ProviderPassport;
import com.alligator.market.domain.provider.registry.ProviderRegistry;

import com.alligator.market.domain.provider.model.vo.ProviderCode;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Сервис проекции паспортов провайдеров из контекста приложения в БД.
 *
 * <p><b>Логика</b></p>
 * <ul>
 *     <li>Источник истины — контекст приложения.</li>
 *     <li>{@link ProviderRegistry} извлекает из контекста приложения паспорта провайдеров, индексированные
 *     по коду провайдера.</li>
 *     <li>{@link ProviderPassportReadStore} извлекает коды провайдеров, для которых в БД есть паспорта.</li>
 *     <li>{@link ProviderPassportWriteStore} пакетно удаляет из БД паспорта, соответствующие устаревшим
 *     кодам провайдеров.</li>
 *     <li>{@link ProviderPassportWriteStore} осуществляет пакетный UPSERT паспортов из контекста.</li>
 * </ul>
 *
 * <p><b>Преимущества пакетной записи:</b></p>
 * <ul>
 *     <li>Минимум обращений к БД → меньше сетевых задержек и нагрузка на пул соединений.</li>
 *     <li>Прямой UPSERT/DELETE без ORM-накладных расходов → быстрее и проще предсказать нагрузку.</li>
 *     <li>Стабильная атомарность операции → все изменения выполняются единым набором пакетных команд.</li>
 *     <li>Единый контракт write-store → легче поддерживать и масштабировать процесс.</li>
 * </ul>
 */
@SuppressWarnings("ClassCanBeRecord")
public class ProviderPassportDbProjection {

    /* Сканер контекста. */
    private final ProviderRegistry providerRegistry;

    /* Репозиторий паспортов. */
    private final ProviderPassportReadStore repository;

    /* Порт записи read model паспортов. */
    private final ProviderPassportWriteStore writeStore;

    /* Конструктор. */
    public ProviderPassportDbProjection(ProviderRegistry providerRegistry,
                                        ProviderPassportReadStore repository,
                                        ProviderPassportWriteStore writeStore) {
        Objects.requireNonNull(providerRegistry, "providerRegistry must not be null");
        Objects.requireNonNull(repository, "repository must not be null");
        Objects.requireNonNull(writeStore, "writeStore must not be null");

        this.providerRegistry = providerRegistry;
        this.repository = repository;
        this.writeStore = writeStore;
    }

    /**
     * Обновить проекцию (одна атомарная транзакция).
     */
    public void refresh() {
        // 1) Извлекаем мапу с паспортами из контекста (индексация по кодам провайдеров)
        Map<ProviderCode, ProviderPassport> ctxPassports = providerRegistry.passportsByCode();
        // 2) Извлекаем коды провайдеров для хранящихся в БД паспортов
        Set<ProviderCode> dbProviderCodes = new LinkedHashSet<>(repository.findAllCodes());

        // Если в контексте пусто — чистим таблицу и выходим
        if (ctxPassports.isEmpty()) {
            if (!dbProviderCodes.isEmpty()) {
                writeStore.deleteByCodes(dbProviderCodes);
            }
            return;
        }

        // 3) Вычисляем устаревшие коды (есть в БД, нет в контексте) и удаляем связанные паспорта
        Set<ProviderCode> obsolete = new LinkedHashSet<>(dbProviderCodes);
        obsolete.removeAll(ctxPassports.keySet());
        if (!obsolete.isEmpty()) {
            writeStore.deleteByCodes(obsolete);
        }

        // 4) UPSERT всех паспортов из контекста (новые/изменившиеся обновятся, актуальные — без изменений)
        writeStore.upsertAll(ctxPassports);
        // Готово
    }
}
