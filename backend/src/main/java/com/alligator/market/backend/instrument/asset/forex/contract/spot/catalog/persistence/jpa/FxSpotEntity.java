package com.alligator.market.backend.instrument.asset.forex.contract.spot.catalog.persistence.jpa;

import com.alligator.market.backend.instrument.catalog.persistence.jpa.InstrumentEntity;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.catalog.persistence.jpa.CurrencyEntity;
import com.alligator.market.backend.instrument.asset.forex.contract.spot.catalog.persistence.jpa.converter.FxSpotTenorConverter;
import com.alligator.market.domain.instrument.model.vo.InstrumentCode;
import com.alligator.market.domain.instrument.model.vo.InstrumentSymbol;
import com.alligator.market.domain.instrument.model.AssetClass;
import com.alligator.market.domain.instrument.model.ContractType;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;
import com.alligator.market.domain.instrument.asset.forex.contract.spot.codec.FxSpotCodec;
import com.alligator.market.domain.instrument.asset.forex.contract.spot.model.InstrumentFxSpot;
import com.alligator.market.domain.instrument.asset.forex.contract.spot.model.FxSpotTenor;
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
 * JPA-сущность финансового инструмента FOREX_SPOT.
 *
 * <p>Назначение: хранение и представление финансового инструмента FOREX_SPOT в базе данных;
 * поля сущности соответствуют доменной модели инструмента FOREX_SPOT {@link InstrumentFxSpot}.</p>
 *
 * <p>Пояснение некоторых аннотаций:</p>
 * <ul>
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
        )
})
@Table(
        name = "fx_spot",
        uniqueConstraints = {
                // Поля, задающие бизнес-уникальность инструмента FOREX_SPOT
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
public class FxSpotEntity extends InstrumentEntity {

    /**
     * Базовая валюта инструмента (FK на {@code currency.code}).
     *
     * <p>Ключевые моменты:</p>
     * <ul>
     *   <li>Поле задает неизменяемый атрибут инструмента, поэтому {@code updatable=false} и запрет
     *   на переназначение через сеттер {@link Setter}.</li>
     *   <li>{@link ManyToOne}: связь по внешнему ключу (многие FOREX_SPOT могут ссылаться на одну валюту).</li>
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
    @Convert(converter = FxSpotTenorConverter.class)
    @Column(
            name = "tenor", length = 5,
            nullable = false,
            updatable = false
    )
    private FxSpotTenor tenor;

    /**
     * Количество знаков после запятой в котировке инструмента FOREX_SPOT (по умолчанию).
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

        final InstrumentSymbol symbol = FxSpotCodec.fxSpotSymbol(baseCode, quoteCode, tenor);
        final InstrumentCode code = FxSpotCodec.fxSpotCode(baseCode, quoteCode, tenor);

        // Инициализируем родительскую сущность
        initIdentity(code, symbol, AssetClass.FOREX, ContractType.SPOT);
    }
}
