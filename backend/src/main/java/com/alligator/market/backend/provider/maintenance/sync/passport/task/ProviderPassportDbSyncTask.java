package com.alligator.market.backend.provider.maintenance.sync.passport.task;

import com.alligator.market.backend.provider.maintenance.ProviderMaintenanceTask;
import com.alligator.market.backend.provider.maintenance.sync.passport.service.ProviderPassportDbSyncRunner;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Задача обслуживания: синхронизация паспортов провайдеров между контекстом и БД.
 */
@Component
@Order(10)
@RequiredArgsConstructor
public class ProviderPassportDbSyncTask implements ProviderMaintenanceTask {

    /* Backend-сервис, запускающий доменный сервис синхронизации паспортов. */
    private final ProviderPassportDbSyncRunner passportSyncRunner;

    @Override
    public String code() {
        return "provider-passport-db-sync";
    }

    @Override
    public void run() {
        passportSyncRunner.runSync();
    }

}
