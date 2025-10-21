package com.alligator.market.domain.provider.reconciliation;

import com.alligator.market.domain.provider.reconciliation.dto.ProviderSnapshot;

import java.util.Collection;

/**
 * Контракт DAO синхронизации провайдеров в базе данных.
 */
public interface ProviderSyncDao {

    /**
     * Удалить записи провайдеров по набору технических кодов.
     */
    void deleteByCodes(Collection<String> codes);

    /**
     * Выполнить пакетный UPSERT снимков провайдеров.
     */
    void upsertAll(Collection<ProviderSnapshot> snapshots);
}
