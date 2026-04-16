package com.alligator.market.backend.instrument.asset.forex.fxspot.catalog.persistence.jpa;

import com.alligator.market.domain.instrument.base.vo.InstrumentCode;
import com.alligator.market.domain.instrument.asset.forex.fxspot.FxSpot;

import java.util.Objects;

/**
 *  Маппер преобразования между JPA-сущностью и доменной моделью для FOREX_SPOT инструмента.
 *
 *  <p>Является "чистым преобразованием типов".</p>
 */
public final class FxSpotEntityMapper {

    /**
     * Приватный конструктор.
     */
    private FxSpotEntityMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Доменная модель -> новая JPA-сущность инструмента FOREX_SPOT.
     */
    public static FxSpotEntity toNewEntity(FxSpot model) {
        Objects.requireNonNull(model, "FxSpot model must not be null");

        // Создаем JPA-сущность, используя специальный безопасный конструктор
        FxSpotEntity entity = new FxSpotEntity(
                model.base().code(),
                model.quote().code(),
                model.tenor()
        );

        // Заполняем изменяемые поля сущности из переданной модели
        applyToEntity(model, entity);

        return entity;
    }

    /**
     * Обновляет изменяемые поля JPA-сущности из доменной модели.
     */
    public static void applyToEntity(FxSpot m, FxSpotEntity e) {
        Objects.requireNonNull(m, "model must not be null");
        Objects.requireNonNull(e, "entity must not be null");

        // Guard check: предотвращаем "тихое" обновление не той сущности.
        InstrumentCode entityCode = e.getCode();
        InstrumentCode modelCode = m.instrumentCode();
        if (!Objects.equals(entityCode, modelCode)) {
            throw new IllegalStateException("Instrument code mismatch: entityCode=" + entityCode.value()
                    + ", modelCode=" + modelCode.value());
        }

        // Копируем изменяемые поля из доменной модели в JPA-сущность
        e.setDefaultQuoteFractionDigits(m.defaultQuoteFractionDigits());
    }

    /**
     * JPA-сущность -> доменная модель FOREX_SPOT.
     */
    public static FxSpot toDomain(FxSpotEntity e) {
        Objects.requireNonNull(e, "entity must not be null");

        // Поле defaultQuoteFractionDigits не является идентичностью сущности и теоретически может быть null,
        // однако бизнес логика доменной модели не предполагает возможность null => нужна проверка.
        int digits = Objects.requireNonNull(e.getDefaultQuoteFractionDigits(),
                "defaultQuoteFractionDigits must not be null");

        // Собираем и возвращаем доменную модель
        return new FxSpot(
                e.getBaseCurrencyCode(),
                e.getQuoteCurrencyCode(),
                e.getTenor(),
                digits
        );
    }
}
