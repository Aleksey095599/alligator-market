package com.alligator.market.backend.provider.maintenance.bootstrap;

import com.alligator.market.backend.config.audit.context.AuditContext;
import com.alligator.market.backend.config.audit.context.AuditContextHolder;
import com.alligator.market.backend.provider.maintenance.ProviderMaintenanceOrchestrator;
import com.alligator.market.backend.provider.maintenance.sync.passport.service.ProviderPassportDbSyncRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Bootstrap-компонент: запускает процесс обслуживания провайдеров рыночных данных при старте приложения.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProviderMaintenanceBootstrap implements ApplicationRunner {

    /* Оркестратор процессов обслуживания провайдеров. */
    private final ProviderMaintenanceOrchestrator orchestrator;

    /* Флаг: запускать ли обслуживание провайдеров при старте приложения (по умолчанию включено). */
    @Value("${provider.maintenance.on-startup:true}")
    private boolean runOnStartup;

    /* Флаг: "падать" ли приложению при сбое хотя бы одного из процессов обслуживания (по умолчанию выключено). */
    @Value("${provider.maintenance.fail-fast:false}")
    private boolean failFast;

    @Override
    public void run(ApplicationArguments args) {
        // Если обслуживание при запуске отключено, выводим сообщение в логи
        if (!runOnStartup) {
            log.info("Provider maintenance on startup is disabled (property 'provider.maintenance.on-startup=false')");
            return;
        }

        log.info("Starting provider maintenance on application startup");

        // Задаем контекст для аудита
        AuditContext ctx = new AuditContext(AuditContextHolder.SYSTEM_ACTOR, "bootstrap:provider-sync");

        try {
            // Запускаем процедуру синхронизации с контекстом для аудита
            AuditContextHolder.runWith(ctx, syncService::runSync);
            log.info("Provider synchronization completed successfully");
        } catch (Exception ex) {
            // Если возникает ошибка:
            // 1) фиксируем сам факт ошибки со стеком
            log.error("Provider synchronization failed on startup", ex);

            if (failFast) {
                // 2а) Если строгий режим (fail-fast:true) – прерываем запуск
                log.error("Application startup aborted (property 'provider.sync.fail-fast=true')");
                throw new IllegalStateException("Provider synchronization failed on startup (fail-fast)", ex);
            } else {
                // 2b) мягкий режим (fail-fast:false) – продолжаем запуск без синхронизации
                log.warn("Continuing application startup without provider synchronization" +
                        " (property 'provider.sync.fail-fast=false')");

            }
        }
    }
}
