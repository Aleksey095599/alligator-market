package com.alligator.market.domain.provider.reconciliation.sync.service;

import com.alligator.market.domain.provider.contract.passport.ProviderPassport;
import com.alligator.market.domain.provider.reconciliation.db.dao.ProviderPassportSyncDao;
import com.alligator.market.domain.provider.reconciliation.context.scanner.ProviderContextScanner;
import com.alligator.market.domain.provider.repository.passport.ProviderPassportRepository;

import com.alligator.market.domain.provider.code.ProviderCode;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Сервис синхронизации паспортов провайдеров между контекстом приложения и БД.
 *
 * <p><b>Логика сервиса</b></p>
 * <ul>
 *     <li>Источник истины — контекст приложения.</li>
 *     <li>Сканер контекста {@link ProviderContextScanner} извлекает из контекста приложения паспорта провайдеров,
 *         индексированные по коду провайдера.</li>
 *     <li>Репозиторий {@link ProviderPassportRepository} извлекает коды провайдеров,
 *         для которых в БД есть паспорта.</li>
 *     <li>DAO {@link ProviderPassportSyncDao} пакетно удаляет из БД паспорта,
 *         соответствующие устаревшим кодам провайдеров.</li>
 *     <li>DAO {@link ProviderPassportSyncDao} осуществляет пакетный UPSERT паспортов из контекста.</li>
 * </ul>
 *
 * <p><b>Преимущества применения DAO для пакетной синхронизации паспортов провайдеров:</b></p>
 * <ul>
 *     <li>Минимум обращений к БД → меньше сетевых задержек и нагрузка на пул соединений.</li>
 *     <li>Прямой UPSERT/DELETE без ORM-накладных расходов → быстрее и проще предсказать нагрузку.</li>
 *     <li>Стабильная атомарность операции → все изменения выполняются единым набором пакетных команд.</li>
 *     <li>Единый контракт для синхронизации → легче поддерживать и масштабировать процесс.</li>
 * </ul>
 */
@SuppressWarnings("ClassCanBeRecord")
public class ProviderPassportSynchronizer {

    /* Сканер контекста. */
    private final ProviderContextScanner contextScanner;

    /* Репозиторий паспортов. */
    private final ProviderPassportRepository repository;

    /* DAO для прямых пакетных операций с паспортами. */
    private final ProviderPassportSyncDao syncDao;

    /* Конструктор. */
    public ProviderPassportSynchronizer(ProviderContextScanner contextScanner,
                                        ProviderPassportRepository repository,
                                        ProviderPassportSyncDao syncDao) {
        this.contextScanner = contextScanner;
        this.repository = repository;
        this.syncDao = syncDao;
    }

    /**
     * Выполнить синхронизацию (одна атомарная транзакция).
     */
    public void synchronize() {
        // 1) Извлекаем мапу с паспортами из контекста (индексация по кодам провайдеров)
        Map<ProviderCode, ProviderPassport> ctxPassports = contextScanner.providerPassports();
        // 2) Извлекаем коды провайдеров для хранящихся в БД паспортов
        Set<ProviderCode> dbProviderCodes = new LinkedHashSet<>(repository.findAllCodes());

        // Если в контексте пусто — чистим таблицу и выходим
        if (ctxPassports.isEmpty()) {
            if (!dbProviderCodes.isEmpty()) {
                syncDao.deleteByCodes(dbProviderCodes);
            }
            return;
        }

        // 3) Вычисляем устаревшие коды (есть в БД, нет в контексте) и удаляем связанные паспорта
        Set<ProviderCode> obsolete = new LinkedHashSet<>(dbProviderCodes);
        obsolete.removeAll(ctxPassports.keySet());
        if (!obsolete.isEmpty()) {
            syncDao.deleteByCodes(obsolete);
        }

        // 4) UPSERT всех паспортов из контекста (новые/изменившиеся обновятся, актуальные — без изменений)
        syncDao.upsertAll(ctxPassports);
        // Готово
    }
}
