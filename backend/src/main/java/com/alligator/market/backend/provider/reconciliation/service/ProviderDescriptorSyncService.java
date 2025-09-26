package com.alligator.market.backend.provider.reconciliation.service;

import com.alligator.market.domain.provider.reconciliation.ProviderDescriptorSynchronizer;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

/** Сервис запускает доменную логику реконсиляции дескрипторов провайдеров рыночных данных. */
@Service
public class ProviderDescriptorSyncService {

    private final ProviderDescriptorSynchronizer sync;
    @Transactional
    public void runSync() { sync.synchronize(); }
}
