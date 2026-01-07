package com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.jpa;

import com.alligator.market.backend.instrument.type.forex.currency.catalog.persistence.jpa.CurrencyEntity;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;

import java.util.Objects;

/**
 * Сборщик JPA-сущности из доменной модели инструмента FX_SPOT.
 */
public final class FxSpotEntityAssembler {

    /**
     * Приватный конструктор.
     */
    private FxSpotEntityAssembler() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Создает новую JPA-сущность на основе доменной модели и сущностей валют.
     */
    public static FxSpotEntity newEntity(FxSpot model, CurrencyEntity baseEntity, CurrencyEntity quoteEntity) {
        Objects.requireNonNull(model, "FxSpot model must not be null");
        Objects.requireNonNull(baseEntity, "entity of base currency must not be null");
        Objects.requireNonNull(quoteEntity, "entity of quote currency must not be null");

        // Создаем JPA-сущность, используя специальный безопасный конструктор
        FxSpotEntity entity = new FxSpotEntity(
                baseEntity,
                quoteEntity,
                model.tenor()
        );

        // Заполняем изменяемые поля сущности из переданной модели
        apply(model, entity);

        return entity;
    }

    /**
     * Обновляет изменяемые поля JPA-сущности из доменной модели.
     */
    public static void apply(FxSpot m, FxSpotEntity e) {
        Objects.requireNonNull(m, "model must not be null");
        Objects.requireNonNull(e, "entity must not be null");

        // Модель и сущность должны соответствовать одному и тому же инструменту
        if (!e.getCode().equals(m.instrumentCode().value())) {
            throw new IllegalStateException("Instrument code mismatch: entity – " + e.getCode() + ", model – " +
                    m.instrumentCode().value());
        }

        // Копируем изменяемые поля
        e.setDefaultQuoteFractionDigits(m.defaultQuoteFractionDigits());
    }

}
