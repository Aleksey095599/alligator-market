package com.alligator.market.backend.provider.catalog.descriptor.persistence.jpa;

/**
 * Возможные статусы дескриптора провайдера.
 * Статусы меняются в результате работы процесса реконсиляции.
 */
public enum DescriptorStatus {
    ACTIVE,
    REPLACED,
    MISSED
}
