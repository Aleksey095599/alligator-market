package com.alligator.market.backend.provider.maintenance.orchestration.bootstrap;

import com.alligator.market.backend.audit.context.AuditContext;
import com.alligator.market.backend.audit.context.AuditContextHolder;
import com.alligator.market.backend.provider.maintenance.properties.ProviderMaintenanceProperties;
import com.alligator.market.backend.provider.maintenance.orchestration.report.ProviderMaintenanceReport;
import com.alligator.market.backend.provider.maintenance.orchestration.service.ProviderMaintenanceOrchestrator;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Bootstrap-компонент: запускает процесс обслуживания провайдеров рыночных данных при старте приложения.
 */
@Slf4j
public class ProviderMaintenanceBootstrap implements ApplicationRunner {

    /* Оркестратор процессов обслуживания провайдеров. */
    private final ProviderMaintenanceOrchestrator orchestrator;

    /* Единый источник конфигурации для provider maintenance. */
    private final ProviderMaintenanceProperties props;

    /* Конструктор. */
    public ProviderMaintenanceBootstrap(
            ProviderMaintenanceOrchestrator orchestrator,
            ProviderMaintenanceProperties props
    ) {
        this.orchestrator = Objects.requireNonNull(orchestrator, "orchestrator must not be null");
        this.props = Objects.requireNonNull(props, "props must not be null");
    }

    @Override
    public void run(ApplicationArguments args) {
        final boolean runOnStartup = props.isOnStartup();
        final boolean failFast = props.isFailFast();

        if (!runOnStartup) {
            log.info("Provider maintenance on startup is disabled (property 'provider.maintenance.on-startup=false')");
            return;
        }

        log.info("Starting provider maintenance on application startup");

        AuditContext ctx = new AuditContext(AuditContextHolder.SYSTEM_ACTOR, "bootstrap:provider-maintenance");

        ProviderMaintenanceReport report;

        try {
            report = AuditContextHolder.runWith(ctx, orchestrator::runAll);
        } catch (Exception ex) {
            // Это инфраструктурная/системная ошибка, не “ошибка задач”
            log.error("Provider maintenance failed on startup", ex);

            if (failFast) {
                log.error("Application startup aborted (property 'provider.maintenance.fail-fast=true')");
                throw new IllegalStateException("Provider maintenance failed on startup (fail-fast)", ex);
            }

            log.warn("Continuing application startup without provider maintenance " +
                    "(property 'provider.maintenance.fail-fast=false')");
            return;
        }

        if (report.hasFailures()) {
            log.error("Provider maintenance completed with failures: success={}, failed={}, skipped={}",
                    report.successCount(), report.failedCount(), report.skippedCount());

            report.failed().forEach(failed -> {
                Throwable err = failed.error();
                String errMsg = (err == null)
                        ? "<unknown>"
                        : err.getClass().getSimpleName() + ": " + err.getMessage();
                log.error("Failed task: {} -> {}", failed.code(), errMsg);
            });

            if (failFast) {
                Throwable first = report.failed().get(0).error();
                throw new IllegalStateException("Provider maintenance completed with failures (fail-fast)", first);
            }

            log.warn("Continuing application startup despite provider maintenance failures " +
                    "(property 'provider.maintenance.fail-fast=false')");
            return;
        }

        log.info("Provider maintenance completed successfully");
    }
}
