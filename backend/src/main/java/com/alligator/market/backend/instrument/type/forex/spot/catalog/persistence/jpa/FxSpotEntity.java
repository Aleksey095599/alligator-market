package com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.jpa;

import com.alligator.market.backend.instrument.base.persistence.jpa.InstrumentBaseEntity;
import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa.CurrencyEntity;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.spot.codec.FxSpotCodec;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpotValueDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

import java.util.Objects;

/**
 * JPA-сущность финансового инструмента FX_SPOT {@link FxSpot} с проверками целостности данных валютной пары.
 */
@Entity
@Check(constraints = "(base_currency <> quote_currency) " +
        "AND (quote_fraction_digits BETWEEN 0 AND 10) " +
        "AND (value_date IN ('TOD','TOM','SPOT'))"
)
@Table(
        name = "fx_spot",
        uniqueConstraints = {
        @UniqueConstraint(name = "uq_fx_spot_pair_value_date",
                columnNames = {"base_currency", "quote_currency", "value_date"})
        },
        indexes = {
                @Index(name = "idx_fx_spot_base", columnList = "base_currency"),
                @Index(name = "idx_fx_spot_quote", columnList = "quote_currency")
        }
)
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // ← скрываем JPA-конструктор
public class FxSpotEntity extends InstrumentBaseEntity {

    /** Уникальный код базовой валюты (FK на "code" в таблице "currency"). */
    @Setter(AccessLevel.NONE) // ← Поле нельзя переназначать сеттером, задаётся один раз через конструктор
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "base_currency", referencedColumnName = "code",
            foreignKey = @ForeignKey(name = "fk_fx_spot_base"), updatable = false, nullable = false)
    private CurrencyEntity baseCurrency;

    /** Уникальный код котируемой валюты (FK на "code" в таблице "currency"). */
    @Setter(AccessLevel.NONE) // ← Поле нельзя переназначать сеттером, задаётся один раз через конструктор
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quote_currency", referencedColumnName = "code",
            foreignKey = @ForeignKey(name = "fk_fx_spot_quote"), updatable = false, nullable = false)
    private CurrencyEntity quoteCurrency;

    /** Код даты расчетов. */
    @Setter(AccessLevel.NONE) // ← Поле нельзя переназначать сеттером, задаётся один раз через конструктор
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "value_date", length = 4, updatable = false, nullable = false)
    private FxSpotValueDate valueDate;

    /** Количество знаков после запятой для курса. */
    @NotNull
    @Min(0)
    @Max(10)
    @Column(name = "quote_fraction_digits", nullable = false)
    private Integer defaultQuoteFractionDigits;

    /** Специальный конструктор — единственный безопасный способ создать сущность. */
    public FxSpotEntity(CurrencyEntity baseCurrency,
                        CurrencyEntity quoteCurrency,
                        FxSpotValueDate valueDate) {
        // ↓↓ Базовые проверки
        Objects.requireNonNull(baseCurrency, "baseCurrency must not be null");
        Objects.requireNonNull(quoteCurrency, "quoteCurrency must not be null");
        Objects.requireNonNull(valueDate, "valueDate must not be null");

        // Валюты не должны совпадать
        if (baseCurrency.getCode().equals(quoteCurrency.getCode())) {
            throw new IllegalArgumentException("base and quote currencies must be different");
        }

        this.baseCurrency = baseCurrency;
        this.quoteCurrency = quoteCurrency;
        this.valueDate = valueDate;

        // ↓↓ Создаем код и символ инструмента
        final String code = FxSpotCodec.fxSpotCode(
                this.baseCurrency.getCode(),
                this.quoteCurrency.getCode(),
                this.valueDate
        );
        final String symbol = FxSpotCodec.fxSpotSymbol(
                this.baseCurrency.getCode(),
                this.quoteCurrency.getCode(),
                this.valueDate
        );

        // Однократно инициализируем идентичность сущности базового класса
        initIdentity(code, symbol, InstrumentType.FX_SPOT);
    }
}
