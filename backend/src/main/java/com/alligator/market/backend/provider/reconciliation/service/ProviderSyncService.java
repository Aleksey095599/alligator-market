package com.alligator.market.backend.provider.reconciliation.service;

import com.alligator.market.domain.provider.reconciliation.ProviderSynchronizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис запускает доменную логику синхронизации данных провайдеров {@link ProviderSynchronizer}.
 * Метод {@code runSync} отмечен как {@code @Transactional}.
 */
@Service
@RequiredArgsConstructor
public class ProviderSyncService {

    /* Доменная логика синхронизации. */
    private final ProviderSynchronizer descriptorSynchronizer;

    /**
     * Запустить процесс синхронизации.
     */
    @Transactional
    public void runSync() {
        descriptorSynchronizer.synchronize();
    }
}
