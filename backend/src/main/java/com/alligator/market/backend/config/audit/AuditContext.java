package com.alligator.market.backend.config.audit;

/**
 * Контекст аудита приложения.
 */
public record AuditContext(

        String actorId,
        String via
) {}


