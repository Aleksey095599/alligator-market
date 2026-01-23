package com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.jpa;

import com.alligator.market.backend.instrument.base.persistence.jpa.InstrumentBaseEntity;
import com.alligator.market.backend.instrument.type.forex.currency.catalog.persistence.jpa.CurrencyEntity;
import com.alligator.market.domain.instrument.code.InstrumentCode;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.currency.code.CurrencyCode;
import com.alligator.market.domain.instrument.type.forex.spot.codec.FxSpotCodec;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpotTenor;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.Checks;

import java.util.Objects;

/**
 * JPA-сущность финансового инструмента FX_SPOT.
 *
 * <p>Ключевые моменты:</p>
 * <ul>
 *     <li>Поля сущности соответствуют доменной модели инструмента FX_SPOT {@link FxSpot}.</li>
 *     <li>{@link PrimaryKeyJoinColumn}: PK таблицы {@code fx_spot} является одновременно FK на PK таблицы
 *     {@code instrument}; таблица {@code fx_spot} является наследницей таблицы {@code instrument}, которая содержит
 *     общие поля для всех финансовых инструментов.</li>
 *     <li>{@link NoArgsConstructor} с {@code PROTECTED}: конструктор без аргументов нужен только для ORM;
 *     вручную сущность создается через специализированный конструктор.</li>
 * </ul>
 */
@Entity
@Checks({
        @Check(
                name = "chk_fx_spot_base_quote_diff",
                constraints = "base_currency <> quote_currency"
        ),
        @Check(
                name = "chk_fx_spot_digits_range",
                constraints = "quote_fraction_digits BETWEEN 0 AND 10"
        ),
        @Check(
                name = "chk_fx_spot_tenor_allowed",
                constraints = "tenor IN ('TOD','TOM','SPOT')"
        )
})
@Table(
        name = "fx_spot",
        uniqueConstraints = {
                // Поля, задающие бизнес-уникальность инструмента FX_SPOT
                @UniqueConstraint(name = "uq_fx_spot_pair_tenor",
                        columnNames = {"base_currency", "quote_currency", "tenor"})
        },
        indexes = {
                // Индекс на FK-колонке ускоряет операции
                @Index(name = "idx_fx_spot_base", columnList = "base_currency"),
                // Индекс на FK-колонке ускоряет операции
                @Index(name = "idx_fx_spot_quote", columnList = "quote_currency")
        }
)
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FxSpotEntity extends InstrumentBaseEntity {

    /**
     * Базовая валюта инструмента (FK на {@code currency.code}).
     *
     * <p>Ключевые моменты:</p>
     * <ul>
     *   <li>Поле задает неизменяемый атрибут инструмента, поэтому {@code updatable=false} и запрет
     *   на переназначение через сеттер {@link Setter}.</li>
     *   <li>{@link ManyToOne}: связь по внешнему ключу (многие FX_SPOT могут ссылаться на одну валюту).</li>
     *   <li>{@link JoinColumn}: FK-колонка {@code base_currency} ссылается на {@code currency.code}.</li>
     *   <li>{@code fetch = LAZY}: валюту загружаем только при обращении к полю.</li>
     *   <li>{@code optional = false}: ссылка обязательна.</li>
     * </ul>
     */
    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "base_currency", referencedColumnName = "code",
            foreignKey = @ForeignKey(name = "fk_fx_spot_base"),
            nullable = false,
            updatable = false
    )
    private CurrencyEntity baseCurrency;

    /**
     * Котируемая валюта инструмента (FK на {@code currency.code}).
     *
     * <p>Ключевые моменты аналогичны {@link #baseCurrency}.</p>
     */
    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "quote_currency", referencedColumnName = "code",
            foreignKey = @ForeignKey(name = "fk_fx_spot_quote"),
            nullable = false,
            updatable = false
    )
    private CurrencyEntity quoteCurrency;

    /**
     * Тенор даты расчетов.
     *
     * <p>Поле задает неизменяемый атрибут инструмента, поэтому {@code updatable=false} и
     * запрет на переназначение через сеттер {@code @Setter(AccessLevel.NONE)}.</p>
     */
    @Setter(AccessLevel.NONE)
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(
            name = "tenor", length = 4,
            nullable = false,
            updatable = false
    )
    private FxSpotTenor tenor;

    /**
     * Количество знаков после запятой в котировке инструмента FX_SPOT (по умолчанию).
     *
     * <p>На уровне кода задан дефолт {@code 4} – наиболее распространенное значение на практике.
     * Рекомендуется закрепить {@code DEFAULT 4} в БД миграцией.</p>
     */
    @NotNull
    @Min(0)
    @Max(10)
    @Column(
            name = "quote_fraction_digits",
            nullable = false
    )
    private Integer defaultQuoteFractionDigits = 4;

    /**
     * Специальный конструктор — единственный безопасный способ создать сущность.
     *
     * <p>Проверяет входные данные, фиксирует неизменяемые поля сущности и инициализирует
     * родительскую сущность финансового инструмента.</p>
     */
    public FxSpotEntity(
            CurrencyEntity baseCurrency,
            CurrencyEntity quoteCurrency,
            FxSpotTenor tenor
    ) {
        this.baseCurrency = Objects.requireNonNull(baseCurrency, "baseCurrency must not be null");
        this.quoteCurrency = Objects.requireNonNull(quoteCurrency, "quoteCurrency must not be null");
        this.tenor = Objects.requireNonNull(tenor, "tenor must not be null");

        final CurrencyCode baseCode = baseCurrency.getCode();
        final CurrencyCode quoteCode = quoteCurrency.getCode();

        if (baseCode.equals(quoteCode)) {
            throw new IllegalArgumentException("base and quote currencies must be different");
        }

        final String symbol = FxSpotCodec.fxSpotSymbol(baseCode, quoteCode, tenor);
        final InstrumentCode code = FxSpotCodec.fxSpotCode(baseCode, quoteCode, tenor);

        // Инициализируем родительскую сущность
        initIdentity(code.value(), symbol, InstrumentType.FX_SPOT);
    }
}
