package com.alligator.market.backend.config.audit;

/**
 * Контекст аудита приложения.
 *
 * @param actorId кто осуществляет действие
 * @param via     посредством чего осуществляется действие
 */
public record AuditContext(String actorId, String via) {}


