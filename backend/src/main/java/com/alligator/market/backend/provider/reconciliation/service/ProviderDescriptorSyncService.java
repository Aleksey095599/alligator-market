package com.alligator.market.backend.provider.reconciliation.service;

import com.alligator.market.domain.provider.reconciliation.descriptor.ProviderDescriptorSyncResult;
import com.alligator.market.domain.provider.reconciliation.descriptor.ProviderDescriptorSynchronizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Сервис запускает доменную логику реконсиляции дескрипторов провайдеров рыночных данных. */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProviderDescriptorSyncService {

    /* Доменный сервис синхронизации дескрипторов. */
    private final ProviderDescriptorSynchronizer synchronizer;

    @Transactional
    public void runSync() {
        ProviderDescriptorSyncResult result = synchronizer.synchronize();
        if (result.changed()) {
            log.info("Provider descriptors sync applied: " +
                            "context={}, repoBefore={}, deleted={}, insertedNew={}, reinsertedUpdated={}",
                    result.inContext(),
                    result.inRepoBefore(),
                    result.deleted(),
                    result.insertedNew(),
                    result.reinsertedUpdated());
        } else {
            log.info("Provider descriptors already in sync: count={}", result.inContext());
        }
    }
}
