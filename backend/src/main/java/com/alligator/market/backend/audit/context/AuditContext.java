package com.alligator.market.backend.audit.context;

/**
 * Контекст аудита приложения.
 *
 * <p>Используется в threadLocal-холдере контекста аудита {@link AuditContextHolder}.</p>
 *
 * @param actorId идентификатор актора, инициировавшего действие
 * @param via     источник/канал (посредством чего выполнено действие)
 */
public record AuditContext(
        String actorId,
        String via
) {
}
