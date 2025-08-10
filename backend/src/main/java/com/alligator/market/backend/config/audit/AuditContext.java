package com.alligator.market.backend.config.audit;

/**
 * Контекст аудита на время операции.
 */
public record AuditContext(String actorId, String via) {}


