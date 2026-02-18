package com.alligator.market.backend.provider.maintenance.projection.db.passport.task;

import com.alligator.market.domain.provider.maintenance.task.ProviderMaintenanceTask;
import com.alligator.market.domain.provider.maintenance.projection.db.passport.service.ProviderPassportDbProjection;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Задача обслуживания: обновление проекции паспортов провайдеров в БД по данным из контекста.
 */
public class ProviderPassportDbProjectionTask implements ProviderMaintenanceTask {

    /* Код задачи. */
    public static final String TASK_CODE = "provider-passport-db-projection";

    /* Доменный use-case обновления проекции паспортов. */
    private final ProviderPassportDbProjection projection;

    /* Конструктор. */
    public ProviderPassportDbProjectionTask(ProviderPassportDbProjection projection) {
        this.projection = Objects.requireNonNull(projection, "projection must not be null");
    }

    @Override
    public String code() {
        return TASK_CODE;
    }

    @Override
    @Transactional
    public void run() {
        projection.refresh();
    }
}
