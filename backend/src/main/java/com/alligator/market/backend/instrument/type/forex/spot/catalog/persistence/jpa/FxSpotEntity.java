package com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.jpa;

import com.alligator.market.backend.instrument.base.persistence.jpa.InstrumentBaseEntity;
import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa.CurrencyEntity;
import com.alligator.market.domain.instrument.type.forex.spot.codec.FxSpotCodec;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpotValueDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

/**
 * JPA-сущность финансового инструмента FX_SPOT {@link FxSpot} с проверками целостности данных валютной пары.
 */
@Entity
@Check(constraints = "base_currency <> quote_currency")
@Table(name = "fx_spot", uniqueConstraints = {
        @UniqueConstraint(name = "uq_fx_spot_pair_value_date", columnNames = {
                "base_currency",
                "quote_currency",
                "value_date_code"
        })
})
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
@NoArgsConstructor
public class FxSpotEntity extends InstrumentBaseEntity {

    /** ISO-4217 код базовой валюты (FK на "code" в таблице "currency"). */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "base_currency", referencedColumnName = "code",
            foreignKey = @ForeignKey(name = "fk_fx_spot_base"), updatable = false, nullable = false)
    private CurrencyEntity baseCurrency;

    /** ISO-4217 код котируемой валюты (FK на "code" в таблице "currency"). */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quote_currency", referencedColumnName = "code",
            foreignKey = @ForeignKey(name = "fk_fx_spot_quote"), updatable = false, nullable = false)
    private CurrencyEntity quoteCurrency;

    /** Код даты расчетов. */
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

    /** JPA-callback код перед вставкой. */
    @Override
    protected void onPrePersist() {
        // 1) Генерируем и устанавливаем код инструмента
        String instrumentCode = FxSpotCodec.fxSpotCode(
                baseCurrency.getCode(),
                quoteCurrency.getCode(),
                valueDate
        );
        setCode(instrumentCode);
        // 2) Генерируем и устанавливаем символ инструмента
        String instrumentSymbol = FxSpotCodec.fxSpotSymbol(
                baseCurrency.getCode(),
                quoteCurrency.getCode(),
                valueDate
        );
        setSymbol(instrumentSymbol);
        // 3) Устанавливаем тип инструмента
        setType(InstrumentType.FX_SPOT);
    }
}
