package com.alligator.market.domain.provider.reconciliation;

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
 * <p>Источник истины — контекст приложения: актуальные паспорта извлекается из контекста приложения, после чего БД
 * приводится в соответствие с помощью DAO для прямых пакетных операций.</p>
 *
 * <p><b>Примечание:</b></p>
 * <p>Доменный репозиторий {@link ProviderPassportRepository} используется для получения набора кодов провайдеров,
 * которые являются натуральными ключами для паспортов в БД. Пакетные операции в БД с целью синхронизации
 * выполняет DAO {@link ProviderPassportSyncDao}.</p>
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
        // 1) Извлекаем актуальные паспорта из контекста (индексация по кодам провайдеров)
        Map<ProviderCode, ProviderPassport> ctxPassports = contextScanner.providerPassports();
        // 2) Извлекаем из БД коды провайдеров — натуральные ключи для паспортов
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
        syncDao.upsertAll(ctxPassports.values());
        // Готово
    }
}
