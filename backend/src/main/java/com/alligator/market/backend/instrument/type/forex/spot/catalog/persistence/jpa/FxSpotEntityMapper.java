package com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.jpa;

import com.alligator.market.backend.instrument.type.forex.currency.catalog.persistence.jpa.CurrencyEntity;
import com.alligator.market.backend.instrument.type.forex.currency.catalog.persistence.jpa.CurrencyEntityMapper;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;

import java.util.Objects;

/**
 * Маппер: JPA-сущность ↔ доменная модель.
 */
public class FxSpotEntityMapper {

    /**
     * Приватный конструктор (запрещает создание экземпляров).
     */
    private FxSpotEntityMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Создать новую JPA-сущность из доменной модели.
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

        apply(model, entity); // <-- Заполняем изменяемые поля из переданной модели
        return entity;
    }

    /**
     * JPA-сущность --> доменная модель.
     */
    public static FxSpot toDomain(FxSpotEntity e) {
        Objects.requireNonNull(e, "entity must not be null");

        // defaultQuoteFractionDigits задана как Integer в JPA-сущности и как int в модели --> нужна null проверка
        int digits = Objects.requireNonNull(e.getDefaultQuoteFractionDigits(),
                "defaultQuoteFractionDigits must not be null");

        // Собираем и возвращаем доменную модель
        return new FxSpot(
                CurrencyEntityMapper.toDomain(e.getBaseCurrency()),
                CurrencyEntityMapper.toDomain(e.getQuoteCurrency()),
                e.getTenor(),
                digits
        );
    }

    /**
     * Копирует в JPA-сущность изменяемые поля из доменной модели.
     */
    public static void apply(FxSpot m, FxSpotEntity e) {
        Objects.requireNonNull(m, "model must not be null");
        Objects.requireNonNull(e, "entity must not be null");

        // Модель и сущность должны соответствовать одному и тому же инструменту
        if (!e.getCode().equals(m.instrumentCode().value())) {
            throw new IllegalStateException("Instrument code mismatch: " + e.getCode() + " vs " +
                    m.instrumentCode().value());
        }

        // Копируем в изменяемые поля JPA-сущности поля из доменной модели
        e.setDefaultQuoteFractionDigits(m.defaultQuoteFractionDigits());
    }
}
