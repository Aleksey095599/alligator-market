package com.alligator.market.backend.config.audit;

/**
 * Контекст аудита приложения.
 * Важно помнить, что из коробки Spring Data JPA поддерживает только @CreatedBy/@LastModifiedBy и даты,
 * то есть
 *
 * @param actorId кто осуществляет действие
 * @param via     посредством чего осуществляется действие
 */
public record AuditContext(String actorId, String via) {}


