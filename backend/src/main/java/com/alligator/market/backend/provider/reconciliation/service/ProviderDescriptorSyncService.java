package com.alligator.market.backend.provider.reconciliation.service;

import com.alligator.market.domain.provider.reconciliation.ProviderDescriptorSynchronizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Сервис запускает доменную логику реконсиляции дескрипторов провайдеров рыночных данных. */
@Service
@RequiredArgsConstructor
public class ProviderDescriptorSyncService {

    /* Доменный сервис синхронизации дескрипторов. */
    private final ProviderDescriptorSynchronizer synchronizer;

    @Transactional
    public void runSync() {
        synchronizer.synchronize();
    }
}
