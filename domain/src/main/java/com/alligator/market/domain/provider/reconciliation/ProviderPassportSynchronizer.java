package com.alligator.market.domain.provider.reconciliation;

import com.alligator.market.domain.provider.contract.passport.ProviderPassport;
import com.alligator.market.domain.provider.reconciliation.scanner.context.ProviderContextScanner;
import com.alligator.market.domain.provider.repository.passport.ProviderPassportRepository;

import com.alligator.market.domain.provider.code.ProviderCode;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Сервис синхронизации паспортов провайдеров между контекстом приложения и хранилищем.
 *
 * <p>Источник истины — контекст приложения: набор паспортов извлекается из контекста приложения
 * с помощью {@link ProviderContextScanner}, после чего состояние хранилища приводится к этому набору.</p>
 *
 * <p>Примечание: доменный репозиторий используется только для чтения кодов из хранилища; запись выполняется через
 * {@code syncDao} пакетными {@code DELETE}/{@code UPSERT} напрямую в БД (в обход JPA).</p>
 */
@SuppressWarnings("ClassCanBeRecord")
public class ProviderPassportSynchronizer {

    /* Сканер контекста --> возвращает паспорта провайдеров из контекста приложения. */
    private final ProviderContextScanner contextScanner;

    /* Репозиторий паспортов провайдеров --> возвращает коды провайдеров из БД. */
    private final ProviderPassportRepository repository;

    /* Контракт синхронизации паспортов провайдеров в БД. */
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
        // 1) Читаем паспорта провайдеров контекста и коды провайдеров из БД
        Map<ProviderCode, ProviderPassport> ctx = contextScanner.providerPassports();
        Set<ProviderCode> repoCodes = new LinkedHashSet<>(repository.findAllCodes());

        // Если в контексте пусто – чистим таблицу и выходим
        if (ctx.isEmpty()) {
            if (!repoCodes.isEmpty()) {
                syncDao.deleteByCodes(repoCodes);
            }
            return;
        }

        // 2) Вычисляем устаревшие коды (есть в БД, нет в контексте) и удаляем их
        Set<ProviderCode> obsolete = new LinkedHashSet<>(repoCodes);
        obsolete.removeAll(ctx.keySet());
        if (!obsolete.isEmpty()) {
            syncDao.deleteByCodes(obsolete); // <-- batch DELETE
        }

        // 3) UPSERT всех из контекста (новые + изменившиеся обновятся, одинаковые пройдут без модификации)
        syncDao.upsertAll(ctx.values()); // <-- batch UPSERT
        // Готово
    }
}
