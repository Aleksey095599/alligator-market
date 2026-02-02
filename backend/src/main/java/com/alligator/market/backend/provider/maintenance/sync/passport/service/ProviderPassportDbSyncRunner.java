package com.alligator.market.backend.provider.maintenance.sync.passport.service;

import com.alligator.market.backend.provider.maintenance.sync.passport.task.ProviderPassportDbSyncTask;
import com.alligator.market.domain.provider.maintenance.projection.db.passport.service.ProviderPassportDbProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис для запуска доменнай логики синхронизации паспортов провайдеров {@link ProviderPassportDbProjection}.
 * TODO: Не является ли данный класс лишним? Стоит ли эту логику вынести в {@link ProviderPassportDbSyncTask}.
 */
@Service
@RequiredArgsConstructor
public class ProviderPassportDbSyncRunner {

    /* Доменная логика синхронизации провайдеров. */
    private final ProviderPassportDbProjection synchronizer;

    /**
     * Запустить процесс синхронизации.
     */
    @Transactional
    public void runSync() {
        synchronizer.refresh();
    }
}
