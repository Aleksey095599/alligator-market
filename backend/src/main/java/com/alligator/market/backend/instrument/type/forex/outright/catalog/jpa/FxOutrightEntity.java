package com.alligator.market.backend.instrument.type.forex.outright.catalog.jpa;

import com.alligator.market.backend.common.jpa.BaseEntity;
import com.alligator.market.backend.instrument.model.InstrumentEmbeddable;
import com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.jpa.CurrencyEntity;
import com.alligator.market.domain.instrument.model.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.outright.model.ValueDateCode;
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
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class FxOutrightEntity extends BaseEntity {

    /** Суррогатный PK. */
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Общие атрибуты инструмента. */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "code", column = @Column(name = "instrument_code", length = 32, nullable = false, updatable = false)),
            @AttributeOverride(name = "type", column = @Column(name = "instrument_type", length = 32, nullable = false, updatable = false))
    })
    private InstrumentEmbeddable instrument = new InstrumentEmbeddable();

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
        // 1) Проверка: базовая и котируемая валюты должны быть разные
        if (baseCurrency.getCode().equals(quoteCurrency.getCode())) {
            // TODO: Заменить на собственную ошибку из доменного класса
            throw new IllegalArgumentException("Base and quote currencies must be different");
        }
        // 2) Присваиваем тип финансового инструмента
        instrument.setType(InstrumentType.FX_OUTRIGHT);
        // 3) Генерируем код финансового инструмента
        __generateInstrumentCode();
    }

    // ===============================
    // Вспомогательные классы и методы
    // ===============================

    /** Вспомогательный метод генерации кода инструмента. */
    private void __generateInstrumentCode() {
        String instrumentCode = baseCurrency.getCode() + quoteCurrency.getCode() + "_" + valueDateCode;
        instrument.setCode(instrumentCode);
    }
}
