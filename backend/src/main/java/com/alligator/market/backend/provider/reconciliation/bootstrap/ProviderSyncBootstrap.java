package com.alligator.market.backend.provider.reconciliation.bootstrap;

import com.alligator.market.backend.config.audit.context.AuditContext;
import com.alligator.market.backend.config.audit.context.AuditContextHolder;
import com.alligator.market.backend.provider.reconciliation.service.ProviderSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Bootstrap-компонент: запускает процесс синхронизации провайдеров рыночных данных при старте приложения.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProviderSyncBootstrap implements ApplicationRunner {

    /* Backend реализация сервиса синхронизации. */
    private final ProviderSyncService syncService;

    /* Флаг: запускать ли синхронизацию при старте приложения (по умолчанию сохраняем текущее поведение). */
    @Value("${provider.sync.on-startup:true}")
    private boolean runOnStartup;

    /* Флаг: падать ли приложению при ошибке синхронизации (по умолчанию выключено). */
    @Value("${provider.sync.fail-fast:false}")
    private boolean failFast;

    @Override
    public void run(ApplicationArguments args) {
        if (!runOnStartup) {
            log.info("Provider sync on startup is disabled (property 'provider.sync.on-startup=false')");
            return;
        }

        log.info("Starting provider synchronization on application startup");

        // Задаем контекст для аудита
        AuditContext ctx = new AuditContext(AuditContextHolder.SYSTEM_ACTOR, "bootstrap:provider-sync");

        try {
            AuditContextHolder.runWith(ctx, () -> {
                // Запускаем процедуру синхронизации
                syncService.runSync();
            });

            log.info("Provider synchronization completed successfully");
        } catch (Exception ex) {
            log.error("Provider synchronization failed on startup", ex);
            if (failFast) {
                if (ex instanceof RuntimeException runtimeException) {
                    throw runtimeException;
                }
                throw new IllegalStateException("Provider sync failed", ex);
            }
        }
    }
}
