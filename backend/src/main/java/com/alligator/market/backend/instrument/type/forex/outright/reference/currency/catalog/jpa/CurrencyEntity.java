package com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.jpa;

import com.alligator.market.backend.common.jpa.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Сущность валюты.
 */
@Entity
@Table(
        name = "currency",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_currency_code", columnNames = "code"),
                @UniqueConstraint(name = "uq_currency_name", columnNames = "name")
        }
)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class CurrencyEntity extends BaseEntity {

    /** Суррогатный PK. */
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** ISO-4217 код валюты. */
    @NotBlank
    @Pattern(regexp = "^[A-Z]{3}$")
    @Column(name = "code", length = 3, nullable = false, updatable = false)
    private String code;

    /** Наименование валюты. */
    @NotBlank
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    /** Страна или регион обращения. */
    @NotBlank
    @Column(name = "country", length = 100, nullable = false)
    private String country;

    /** Кол-во знаков после запятой для денежных сумм. */
    @NotNull
    @Min(0)
    @Max(10)
    @Column(name = "decimal", nullable = false)
    private Integer decimal;
}
