package com.alligator.market.backend.provider.reconciliation.service;

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
    private final ProviderDescriptorSynchronizer descriptorSynchronizer;

    /** Запустить процесс синхронизации дескрипторов. */
    @Transactional
    public void runDescriptorSync() {
        descriptorSynchronizer.synchronize();
    }
}
