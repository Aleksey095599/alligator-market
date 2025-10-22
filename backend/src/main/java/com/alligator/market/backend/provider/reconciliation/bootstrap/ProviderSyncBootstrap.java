package com.alligator.market.backend.provider.reconciliation.bootstrap;

import com.alligator.market.backend.config.audit.AuditContext;
import com.alligator.market.backend.config.audit.AuditContextHolder;
import com.alligator.market.backend.provider.reconciliation.service.ProviderSyncService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Bootstrap-компонент: запускает процесс синхронизации данных провайдеров при старте приложения.
 */
@Component
@RequiredArgsConstructor
public class ProviderSyncBootstrap implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(ProviderSyncBootstrap.class);

    /* Backend реализация сервиса синхронизации. */
    private final ProviderSyncService syncService;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting provider synchronization");
        // ↓↓ Добавляем контекст для аудита
        AuditContext previousContext = AuditContextHolder.get();
        AuditContextHolder.set(new AuditContext(AuditContextHolder.SYSTEM_ACTOR, "provider-bootstrap"));
        try {
            syncService.runSync();
            log.info("Provider synchronization completed");
        } catch (RuntimeException ex) {
            log.error("Provider synchronization failed", ex);
            throw ex;
        } finally {
            AuditContextHolder.set(previousContext);
        }
    }
}
