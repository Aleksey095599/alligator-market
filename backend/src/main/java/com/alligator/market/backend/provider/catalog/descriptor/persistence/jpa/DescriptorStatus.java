package com.alligator.market.backend.provider.catalog.descriptor.persistence.jpa;

/**
 * Возможные статусы дескриптора провайдера.
 * Статусы меняются в результате работы процесса реконсиляции.
 */
public enum DescriptorStatus {
    ACTIVE,   // Дескриптор найден в контексте приложения
    REPLACED, // Дескриптор был найден, но атрибуты изменились
    MISSED    // Дескриптор был утрачен
}
