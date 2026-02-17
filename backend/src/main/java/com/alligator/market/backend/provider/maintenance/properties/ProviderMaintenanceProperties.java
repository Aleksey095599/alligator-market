package com.alligator.market.backend.provider.maintenance.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Параметры обслуживания провайдеров (provider maintenance).
 *
 * <p>Примечение: Spring Boot автоматически биндингует сюда настройки с префиксом {@code provider.maintenance.*} из
 * файла настроек приложения.</p>
 *
 * <p>Пример:</p>
 * <pre>
 * provider:
 *   maintenance:
 *     bootstrap:
 *       on-startup: true
 *       fail-fast: false
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

    /* Настройки поведения bootstrap (старт приложения). */
    @Valid
    @NotNull
    private BootstrapProperties bootstrap = new BootstrapProperties();

    /* Настройки задач maintenance по коду: provider.maintenance.tasks.<task-code>.* */
    @Valid
    @NotNull
    private Map<String, TaskProperties> tasks = new HashMap<>();

    /**
     * Удобный метод: проверить, включена ли задача по её коду.
     *
     * <p>Если код задачи отсутствует в настройках, используется {@code defaultEnabled}.</p>
     */
    public boolean isTaskEnabled(String code, boolean defaultEnabled) {
        Objects.requireNonNull(code, "task code must not be null");
        if (code.isBlank()) {
            throw new IllegalArgumentException("task code must not be blank");
        }
        TaskProperties props = tasks.get(code);
        return props == null ? defaultEnabled : props.enabled;
    }

    /**
     * Группа настроек {@code provider.maintenance.bootstrap.*}.
     */
    @Getter
    @Setter
    public static class BootstrapProperties {

        /* provider.maintenance.bootstrap.on-startup */
        private boolean onStartup = true;

        /* provider.maintenance.bootstrap.fail-fast */
        private boolean failFast = false;
    }

    /**
     * Настройки одной конкретной задачи (например, "provider-passport-db-projection").
     */
    @Getter
    @Setter
    public static class TaskProperties {

        /* provider.maintenance.tasks.<task-code>.enabled */
        private boolean enabled = true;
    }
}
