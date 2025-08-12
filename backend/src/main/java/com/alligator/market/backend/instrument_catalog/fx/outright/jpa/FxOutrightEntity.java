package com.alligator.market.backend.instrument_catalog.fx.outright.jpa;

import com.alligator.market.backend.common.jpa.BaseEntity;
import com.alligator.market.backend.instrument_catalog.fx.reference.currency_pair.jpa.CurrencyPairEntity;
import com.alligator.market.domain.instrument.type.fx.outright.model.ValueDateCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity инструмента FX OUTRIGHT.
 */
@Entity
@Table(name = "fx_spot_instruments")
@Getter
@Setter
@NoArgsConstructor
public class FxOutrightEntity extends BaseEntity {

    /** Внутренний код инструмента. */
    @Id
    @Column(name = "internal_code", length = 12)
    private String internalCode;

    /** Ссылка на валютную пару. */
    @ManyToOne(optional = false)
    @JoinColumn(name = "pair_code", referencedColumnName = "pair_code",
            foreignKey = @ForeignKey(name = "fk_fx_spot_pair"))
    private CurrencyPairEntity currencyPair;

    /** Код даты расчетов. */
    @Enumerated(EnumType.STRING)
    @Column(name = "value_date_code", length = 4, nullable = false)
    private ValueDateCode valueDateCode;
}
