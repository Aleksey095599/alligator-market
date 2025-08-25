package com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.jpa;

import com.alligator.market.backend.instrument.catalog.persistence.jpa.InstrumentEntity;
import com.alligator.market.backend.instrument.type.forex.spot.reference.currency.catalog.persistence.jpa.CurrencyEntity;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.spot.model.ValueDateCode;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotSameCurrenciesException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Сущность финансового инструмента FX_OUTRIGHT.
 */
@Entity
@Table(name = "fx_outright")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FxOutrightEntity extends InstrumentEntity {

    /** ISO-4217 код базовой валюты (FK на "code" в таблице "currency"). */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "base_currency", referencedColumnName = "code",
            foreignKey = @ForeignKey(name = "fk_fx_outright_base"), updatable = false, nullable = false)
    private CurrencyEntity baseCurrency;

    /** ISO-4217 код котируемой валюты (FK на "code" в таблице "currency"). */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quote_currency", referencedColumnName = "code",
            foreignKey = @ForeignKey(name = "fk_fx_outright_quote"), updatable = false, nullable = false)
    private CurrencyEntity quoteCurrency;

    /** Код даты расчетов. */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "value_date_code", length = 4, updatable = false, nullable = false)
    private ValueDateCode valueDateCode;

    /** Кол-во знаков после запятой для курса. */
    @NotNull
    @Min(0)
    @Max(10)
    @Column(name = "quote_decimal", nullable = false)
    private Integer quoteDecimal;

    /** JPA-callback код перед вставкой. */
    @Override
    protected void onPrePersist() {
        // 1) Проверяем, что валюты различаются
        if (baseCurrency.getCode().equals(quoteCurrency.getCode())) {
            throw new FxSpotSameCurrenciesException();
        }
        // 2) Устанавливаем тип инструмента
        setType(InstrumentType.FX_OUTRIGHT);
        // 3) Генерируем и устанавливаем код инструмента
        String instrumentCode = baseCurrency.getCode() + quoteCurrency.getCode() + "_" + valueDateCode.name();
        setCode(instrumentCode);
    }
}
