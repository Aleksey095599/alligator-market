package com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.jpa;

import com.alligator.market.backend.instrument.base.persistence.jpa.InstrumentBaseEntity;
import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa.CurrencyEntity;
import com.alligator.market.domain.instrument.type.forex.spot.utility.FxSpotNaming;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.instrument.type.forex.spot.model.ValueDateCode;
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
@Table(name = "fx_spot", uniqueConstraints = {
        @UniqueConstraint(name = "uq_fx_spot_pair_value_date", columnNames = {
                "base_currency",
                "quote_currency",
                "value_date_code"
        })
})
@Check(constraints = "base_currency <> quote_currency")
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
    @Column(name = "value_date_code", length = 4, updatable = false, nullable = false)
    private ValueDateCode valueDateCode;

    /** Количество знаков после запятой для курса. */
    @NotNull
    @Min(0)
    @Max(10)
    @Column(name = "quote_decimal", nullable = false)
    private Integer quoteDecimal;

    /** JPA-callback код перед вставкой. */
    @Override
    protected void onPrePersist() {
        // 1) Генерируем и устанавливаем код инструмента
        String instrumentCode = FxSpotNaming.fxSpotCode(
                baseCurrency.getCode(),
                quoteCurrency.getCode(),
                valueDateCode
        );
        setCode(instrumentCode);
        // 2) Генерируем и устанавливаем символ инструмента
        String instrumentSymbol = FxSpotNaming.fxSpotSymbol(
                baseCurrency.getCode(),
                quoteCurrency.getCode(),
                valueDateCode
        );
        setSymbol(instrumentSymbol);
        // 3) Устанавливаем тип инструмента
        setType(InstrumentType.FX_SPOT);
    }
}
