package com.alligator.market.backend.provider.reconciliation.bootstrap;

import com.alligator.market.backend.config.audit.context.AuditContext;
import com.alligator.market.backend.config.audit.context.AuditContextHolder;
import com.alligator.market.backend.provider.reconciliation.service.ProviderSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting provider synchronization");

        // Задаем контекст для аудита
        AuditContext ctx = new AuditContext(AuditContextHolder.SYSTEM_ACTOR, "bootstrap:provider-sync");

        AuditContextHolder.runWith(ctx, () -> {
            syncService.runSync();
            log.info("Provider synchronization completed");
        });
    }
}
