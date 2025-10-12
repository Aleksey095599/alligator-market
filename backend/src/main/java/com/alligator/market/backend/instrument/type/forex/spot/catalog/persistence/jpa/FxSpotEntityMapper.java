package com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.jpa;

import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa.CurrencyEntity;
import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa.CurrencyEntityMapper;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Маппер: JPA-сущность ⇄ доменная модель.
 */
@Component
public class FxSpotEntityMapper {

    /** Приватный конструктор (запрещает создание экземпляров). */
    private FxSpotEntityMapper() { throw new UnsupportedOperationException("Utility class"); }

    /** Создать новую JPA-сущность из доменной модели. */
    public static FxSpotEntity newEntity(FxSpot model, CurrencyEntity baseEntity, CurrencyEntity quoteEntity) {
        // ↓↓ Базовые проверки аргументов
        Objects.requireNonNull(model, "model must not be null");
        Objects.requireNonNull(baseEntity, "entity of base currency must not be null");
        Objects.requireNonNull(quoteEntity, "entity of quote currency must not be null");

        // Создаем JPA-сущность используя специальный конструктор
        FxSpotEntity entity = new FxSpotEntity(
                baseEntity,
                quoteEntity,
                model.valueDate(),
                model.defaultQuoteFractionDigits()
        );

        apply(model, entity); // ← На всякий случай синхронизируем изменяемые поля
        return entity;
    }

    /** JPA-сущность ⇒ доменная модель. */
    public FxSpot toDomain(FxSpotEntity e) {
        Objects.requireNonNull(e, "entity must not be null");

        // defaultQuoteFractionDigits задана как Integer в JPA-сущности и как int в модели ⇒ нужна null проверка
        int digits = Objects.requireNonNull(e.getDefaultQuoteFractionDigits(),
                "defaultQuoteFractionDigits must not be null");

        // Собираем и возвращаем доменную модель
        return new FxSpot(
                CurrencyEntityMapper.toDomain(e.getBaseCurrency()),
                CurrencyEntityMapper.toDomain(e.getQuoteCurrency()),
                e.getValueDate(),
                digits
        );
    }

    /** Копирует в JPA-сущность изменяемые поля из доменной модели. */
    public static void apply(FxSpot model, FxSpotEntity e) {
        // ↓↓ Базовые проверки аргументов
        Objects.requireNonNull(model, "model must not be null");
        Objects.requireNonNull(e, "entity must not be null");

        // Модель и сущность должны соответствовать одному и тому же инструменту
        if (!e.getCode().equals(model.instrumentCode())) {
            throw new IllegalStateException("Instrument code mismatch: " + e.getCode() + " vs " +
                    model.instrumentCode());
        }

        // ↓↓ Копируем в JPA-сущность поля из доменной модели
    }
}

