package com.alligator.market.backend.instrument.type.forex.outright.catalog.jpa;

import com.alligator.market.backend.instrument.catalog.jpa.InstrumentEntity;
import com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.jpa.CurrencyEntity;
import com.alligator.market.domain.instrument.type.forex.outright.model.ValueDateCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity финансового инструмента FX_OUTRIGHT.
 */
@Entity
@Table(name = "fx_outright")
@Getter
@Setter
@NoArgsConstructor
public class FxOutrightEntity extends InstrumentEntity {

    /** ISO-4217 код базовой валюты (FK на "code" в таблице "currency"). */
    @ManyToOne(optional = false)
    @JoinColumn(name = "base_currency", referencedColumnName = "code",
            foreignKey = @ForeignKey(name = "fk_fx_outright_base"))
    private CurrencyEntity baseCurrency;

    /** ISO-4217 код котируемой валюты (FK на "code" в таблице "currency"). */
    @ManyToOne(optional = false)
    @JoinColumn(name = "quote_currency", referencedColumnName = "code",
            foreignKey = @ForeignKey(name = "fk_fx_outright_quote"))
    private CurrencyEntity quoteCurrency;

    /** Код даты расчетов. */
    @Enumerated(EnumType.STRING)
    @Column(name = "value_date_code", length = 4, nullable = false)
    private ValueDateCode valueDateCode;

    /** Кол-во знаков после запятой для курса. */
    @Column(nullable = false)
    @Min(0)
    @Max(10)
    private Integer quoteDecimal;

    /* JPA-callback: генерируем код перед вставкой. */
    @Override
    protected void onPrePersist() {
        __generateInstrumentCode();
    }

    /* JPA-callback: генерируем код перед обновлением. */
    @Override
    protected void onPreUpdate() {
        __generateInstrumentCode();
    }

    /* Генерация внутреннего кода инструмента. */
    private void __generateInstrumentCode() {
        String instrumentCode = baseCurrency.getCode() + quoteCurrency.getCode() + "_" + valueDateCode;
        setCode(instrumentCode);
    }
}
