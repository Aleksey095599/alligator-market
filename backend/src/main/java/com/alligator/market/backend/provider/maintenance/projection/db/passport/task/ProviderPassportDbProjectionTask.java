package com.alligator.market.backend.provider.maintenance.projection.db.passport.task;

import com.alligator.market.backend.provider.maintenance.orchestration.task.ProviderMaintenanceTask;
import com.alligator.market.domain.provider.maintenance.projection.db.passport.service.ProviderPassportDbProjection;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Задача обслуживания: обновление проекции паспортов провайдеров в БД по данным из контекста.
 */
@Component
@Order(10)
@RequiredArgsConstructor
public class ProviderPassportDbProjectionTask implements ProviderMaintenanceTask {

    /* Доменный use-case обновления проекции паспортов. */
    private final ProviderPassportDbProjection projection;

    @Override
    public String code() {
        return "provider-passport-db-projection";
    }

    @Override
    @Transactional
    public void run() {
        projection.refresh();
    }
}
