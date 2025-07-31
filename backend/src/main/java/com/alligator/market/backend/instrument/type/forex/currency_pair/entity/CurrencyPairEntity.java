package com.alligator.market.backend.instrument.type.forex.currency_pair.entity;

import com.alligator.market.backend.common.jpa.BaseEntity;
import com.alligator.market.backend.instrument.type.forex.currency.entity.CurrencyEntity;
import com.alligator.market.domain.instrument.type.forex.currency.Currency;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity для валютной пары.
 */
@Entity
@Table(
        name = "currency_pair",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_pair_code", columnNames = "pair_code")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class CurrencyPairEntity extends BaseEntity {

    /** Суррогатный PK. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ISO-4217 код базовой валюты (FK на {@link Currency#code()}). */
    @ManyToOne(optional = false)
    @JoinColumn(name = "base", referencedColumnName = "code",
            foreignKey = @ForeignKey(name = "fk_pair_base"))
    private CurrencyEntity base;

    /** ISO-4217 код котируемой валюты (FK на {@link Currency#code()}). */
    @ManyToOne(optional = false)
    @JoinColumn(name = "quote", referencedColumnName = "code",
            foreignKey = @ForeignKey(name = "fk_pair_quote"))
    private CurrencyEntity quote;

    /** Условный код валютной пары, составленный из базовой и котируемой валют. */
    @Pattern(regexp = "^[A-Z]{6}$")
    @Column(name = "pair_code", length = 6, nullable = false)
    private String pairCode;

    /** Кол-во знаков после запятой для курса. */
    @Column(nullable = false)
    @Min(0)
    @Max(10)
    private Integer decimal;
}
