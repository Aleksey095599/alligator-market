package com.alligator.market.backend.provider.reconciliation.bootstrap;

import com.alligator.market.backend.config.audit.AuditContext;
import com.alligator.market.backend.config.audit.AuditContextHolder;
import com.alligator.market.backend.provider.reconciliation.service.ProviderDescriptorSyncService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Bootstrap-компонент: запускает сервисы реконсиляции данных о провайдерах рыночных данных при старте приложения.
 */
@Component
@RequiredArgsConstructor
public class ProviderReconciliationBootstrap implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(ProviderReconciliationBootstrap.class);

    /* Сервис для запуска транзакционной синхронизации дескрипторов. */
    private final ProviderDescriptorSyncService syncService;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting provider descriptors synchronization");
        // ↓↓ Добавляем контекст для аудита
        AuditContext previousContext = AuditContextHolder.get();
        AuditContextHolder.set(new AuditContext(AuditContextHolder.SYSTEM_ACTOR, "provider-descriptor-bootstrap"));
        try {
            syncService.runDescriptorSync();
            log.info("Provider descriptors synchronization completed");
        } catch (RuntimeException ex) {
            log.error("Provider descriptors synchronization failed", ex);
            throw ex;
        } finally {
            AuditContextHolder.set(previousContext);
        }
    }
}
