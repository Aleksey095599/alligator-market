package com.alligator.market.backend.provider.maintenance.orchestration.bootstrap;

import com.alligator.market.backend.config.audit.context.AuditContext;
import com.alligator.market.backend.config.audit.context.AuditContextHolder;
import com.alligator.market.backend.provider.maintenance.orchestration.report.ProviderMaintenanceReport;
import com.alligator.market.backend.provider.maintenance.orchestration.service.ProviderMaintenanceOrchestrator;
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
