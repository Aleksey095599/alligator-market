package com.alligator.market.domain.provider.reconciliation;

import com.alligator.market.domain.provider.reconciliation.dto.ProviderSnapshot;
import com.alligator.market.domain.provider.reconciliation.scanner.ProviderContextScanner;
import com.alligator.market.domain.provider.repository.ProviderRepository;

import java.util.*;

/**
 * Доменный сервис синхронизации данных провайдеров в контексте приложения и в базе данных.
 */
public class ProviderSynchronizer {

    /* Сканер контекста → возвращает снимки. */
    private final ProviderContextScanner contextScanner;

    /* Репозиторий → извлекает коды провайдеров. */
    private final ProviderRepository repository;

    /* Контракт синхронизации провайдеров в БД. */
    private final ProviderSyncDao syncDao;

    /* Конструктор. */
    public ProviderSynchronizer(ProviderContextScanner contextScanner,
                                ProviderRepository repository,
                                ProviderSyncDao syncDao) {
        this.contextScanner = contextScanner;
        this.repository = repository;
        this.syncDao = syncDao;
    }

    /** Выполнить синхронизацию (одна атомарная транзакция). */
    public void synchronize() {

        // 1) Читаем снимки из контекста и коды из БД
        Map<String, ProviderSnapshot> ctx = contextScanner.providerSnapshots();
        Set<String> repoCodes = new LinkedHashSet<>(repository.findAllCodes());

        // Если в контексте пусто — чистим таблицу и выходим
        if (ctx.isEmpty()) {
            if (!repoCodes.isEmpty()) {
                syncDao.deleteByCodes(repoCodes);
            }
            return;
        }

        // 2) Вычисляем устаревшие коды (есть в БД, нет в контексте) и удаляем их
        Set<String> obsolete = new LinkedHashSet<>(repoCodes);
        obsolete.removeAll(ctx.keySet());
        if (!obsolete.isEmpty()) {
            syncDao.deleteByCodes(obsolete); // batch DELETE
        }

        // 3) UPSERT всех из контекста (новые + изменившиеся обновятся, одинаковые пройдут без модификации)
        syncDao.upsertAll(ctx.values()); // batch UPSERT
        // Готово
    }
}
