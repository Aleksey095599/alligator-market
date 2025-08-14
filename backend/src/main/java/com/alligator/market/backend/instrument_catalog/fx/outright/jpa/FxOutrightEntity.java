package com.alligator.market.backend.instrument_catalog.fx.outright.jpa;

import com.alligator.market.backend.common.jpa.BaseEntity;
import com.alligator.market.backend.instrument.type.fx.outright.reference.currency.catalog.jpa.CurrencyEntity;
import com.alligator.market.domain.instrument.type.fx.outright.model.ValueDateCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity инструмента FX OUTRIGHT.
 */
@Entity
@Table(name = "fx_outright")
@Getter
@Setter
@NoArgsConstructor
public class FxOutrightEntity extends BaseEntity {

    /** Внутренний код инструмента. */
    @Id
    @Column(name = "internal_code", length = 12)
    private String internalCode;

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
}
