package com.alligator.market.backend.provider.reconciliation.bootstrap;

import com.alligator.market.backend.config.audit.AuditContext;
import com.alligator.market.backend.config.audit.AuditContextHolder;
import com.alligator.market.domain.provider.reconciliation.ProviderDescriptorSynchronizer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Bootstrap-компонент: выполняет синхронизацию дескрипторов провайдеров при старте приложения.
 */
@Component
@RequiredArgsConstructor
@Transactional
public class ProviderDescriptorBootstrap implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(ProviderDescriptorBootstrap.class);

    /* Доменный сервис синхронизации дескрипторов. */
    private final ProviderDescriptorSynchronizer synchronizer;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting provider descriptors synchronization");
        // ↓↓ Добавляем контекст для аудита
        AuditContext previousContext = AuditContextHolder.get();
        AuditContextHolder.set(new AuditContext(AuditContextHolder.SYSTEM_ACTOR, "provider-descriptor-bootstrap"));
        try {
            synchronizer.synchronize();
            log.info("Provider descriptors synchronization completed");
        } catch (RuntimeException ex) {
            log.error("Provider descriptors synchronization failed", ex);
            throw ex;
        } finally {
            AuditContextHolder.set(previousContext);
        }
    }
}
