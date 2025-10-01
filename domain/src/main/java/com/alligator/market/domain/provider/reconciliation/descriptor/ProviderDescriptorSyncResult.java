package com.alligator.market.domain.provider.reconciliation.descriptor;

/**
 * Результат синхронизации дескрипторов.
 * Соответствует логике работы {@link ProviderDescriptorSynchronizer}.
 */
public record ProviderDescriptorSyncResult(
        int inContext,
        int inRepoBefore,
        int deleted,           // codesToDelete.size() + codesToUpdate.size()
        int insertedNew,       // descriptorsToAdd.size()
        int reinsertedUpdated, // codesToUpdate.size()
        boolean changed        // deleted > 0 || insertedNew > 0 || reinsertedUpdated > 0
) {
    public int totalInserted() {
        return insertedNew + reinsertedUpdated;
    }
}
