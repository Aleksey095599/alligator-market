package com.alligator.market.backend.provider.maintenance.passport.sync.service;

import com.alligator.market.domain.provider.maintenance.passport.sync.service.PassportDbSync;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис для запуска доменнай логики синхронизации паспортов провайдеров {@link PassportDbSync}.
 */
@Service
@RequiredArgsConstructor
public class PassportDbSyncRun {

    /* Доменная логика синхронизации провайдеров. */
    private final PassportDbSync synchronizer;

    /**
     * Запустить процесс синхронизации.
     */
    @Transactional
    public void runSync() {
        synchronizer.synchronize();
    }
}
