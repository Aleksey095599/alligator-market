package com.alligator.market.backend.provider.reconciliation.service;

import com.alligator.market.domain.provider.reconciliation.sync.service.ProviderPassportSynchronizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис запускает доменную логику синхронизации данных провайдеров {@link ProviderPassportSynchronizer}.
 */
@Service
@RequiredArgsConstructor
public class ProviderSyncService {

    /* Доменная логика синхронизации провайдеров. */
    private final ProviderPassportSynchronizer synchronizer;

    /**
     * Запустить процесс синхронизации.
     */
    @Transactional
    public void runSync() {
        synchronizer.synchronize();
    }
}
