package com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.jpa;

import com.alligator.market.backend.instrument.base.jpa.InstrumentBaseEntity;
import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa.CurrencyEntity;
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

/**
 * JPA-сущность финансового инструмента FX_SPOT {@link FxSpot}.
 */
@Entity
@Table(name = "fx_spot")
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
        // Устанавливаем тип инструмента
        setType(InstrumentType.FX_SPOT);
        // Генерируем и устанавливаем код инструмента
        String instrumentCode = baseCurrency.getCode() + quoteCurrency.getCode() + "_" + valueDateCode.name();
        setCode(instrumentCode);
    }
}
