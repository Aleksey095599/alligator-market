package com.alligator.market.backend.config.audit.context;

/**
 * Контекст аудита приложения.
 * Содержит два компонента: {@code actorId} — кто выполняет действие, {@code via} — посредством чего (канал/источник).
 *
 * @param actorId идентификатор актора, инициировавшего действие
 * @param via     источник/канал
 * @see AuditContextHolder
 */
public record AuditContext(
        String actorId,
        String via
) {}
