package com.alligator.market.backend.provider.catalog.passport.persistence.jpa;

import com.alligator.market.domain.provider.contract.passport.ProviderPassport;

import java.util.Objects;

/**
 * Маппер: JPA-сущность → доменная модель.
 *
 * <p>Только чтение, так как таблица provider_passport является статичным справочником.</p>
 */
public final class ProviderPassportEntityMapper {

    /**
     * Приватный конструктор (запрещает создание экземпляров).
     */
    private ProviderPassportEntityMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * JPA-сущность --> доменная модель.
     */
    public static ProviderPassport toDomain(ProviderPassportEntity e) {
        Objects.requireNonNull(e, "entity must not be null");

        // Собираем и возвращаем доменную модель паспорта провайдера
        return new ProviderPassport(
                e.getDisplayName(),
                e.getDeliveryMode(),
                e.getAccessMethod(),
                e.isBulkSubscription()
        );
    }
}
