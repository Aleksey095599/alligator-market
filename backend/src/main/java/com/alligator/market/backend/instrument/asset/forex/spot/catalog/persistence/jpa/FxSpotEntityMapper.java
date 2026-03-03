package com.alligator.market.backend.instrument.asset.forex.spot.catalog.persistence.jpa;

import com.alligator.market.backend.instrument.asset.forex.currency.catalog.persistence.jpa.CurrencyEntity;
import com.alligator.market.backend.instrument.asset.forex.currency.catalog.persistence.jpa.CurrencyEntityMapper;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.instrument.asset.forex.contract.spot.model.FxSpot;

import java.util.Objects;

/**
 *  Маппер преобразования между JPA-сущностью и доменной моделью для FX_SPOT инструмента.
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
     * Доменная модель + JPA-сущности валют -> JPA-сущность инструмента FX_SPOT.
     */
    public static FxSpotEntity toNewEntity(FxSpot model, CurrencyEntity baseEntity, CurrencyEntity quoteEntity) {
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
     * JPA-сущность -> доменная модель FX_SPOT.
     */
    public static FxSpot toDomain(FxSpotEntity e) {
        Objects.requireNonNull(e, "entity must not be null");

        // Поле defaultQuoteFractionDigits не является идентичностью сущности и теоретически может быть null,
        // однако бизнес логика доменной модели не предполагает возможность null => нужна проверка.
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
}
