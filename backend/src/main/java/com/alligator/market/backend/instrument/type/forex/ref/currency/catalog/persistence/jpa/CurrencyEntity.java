package com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa;

import com.alligator.market.backend.common.jpa.BaseEntity;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

/**
 * JPA-сущность валюты {@link Currency}.
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
public class CurrencyEntity extends BaseEntity {

    /** Суррогатный PK. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** ISO-4217 код валюты. */
    @NotBlank
    @Pattern(regexp = "^[A-Z]{3}$")
    @NaturalId() // Полезно, так как по сути данное поле это «естественный» неизменяемый ключ
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

    /** Количество знаков после запятой для денежных сумм. */
    @NotNull
    @Min(0)
    @Max(10)
    @Column(name = "default_fraction_digits", nullable = false)
    private Integer defaultFractionDigits;
}
