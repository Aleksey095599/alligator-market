package com.alligator.market.backend.provider.catalog.descriptor.persistence.jpa;

/**
 * Возможные статусы дескриптора провайдера.
 */
public enum DescriptorStatus {

    /** Дескриптор актуален и используется. */
    ACTIVE,

    /** Дескриптор заменён новой версией. */
    REPLACED,

    /** Дескриптор отсутствует у провайдера. */
    MISSED
}
