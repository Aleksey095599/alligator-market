package com.alligator.market.backend.provider.maintenance.orchestration.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Параметры обслуживания провайдеров.
 *
 * <p>Данные параметры автоматически считываются из файла настроек приложения.</p>
 *
 * <p>Пример:</p>
 * <pre>
 * provider:
 *   maintenance:
 *     on-startup: true
 *     fail-fast: false
 *     tasks:
 *       provider-passport-db-projection:
 *         enabled: true
 * </pre>
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "provider.maintenance")
public class ProviderMaintenanceProperties {

    /* Запускать ли maintenance при старте приложения. */
    private boolean onStartup = true;

    /* Прерывать ли запуск приложения, если есть проваленные задачи. */
    private boolean failFast = false;

    /* Настройки задач по коду задачи. */
    private Map<String, TaskProperties> tasks = new HashMap<>();

    public boolean isTaskEnabled(String code, boolean defaultEnabled) {
        Objects.requireNonNull(code, "task code must not be null");
        if (code.isBlank()) {
            throw new IllegalArgumentException("task code must not be blank");
        }

        TaskProperties props = tasks.get(code);
        return props == null ? defaultEnabled : props.enabled;
    }

    @Getter
    @Setter
    public static class TaskProperties {

        /* Включена ли задача. */
        private boolean enabled = true;
    }
}
