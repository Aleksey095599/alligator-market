package com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.persistence.jpa;

import com.alligator.market.backend.common.jpa.BaseEntity;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.CurrencyCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.NaturalId;

import java.util.Objects;

/**
 * JPA-сущность валюты {@link Currency}.
 */
@Entity
@Check(constraints = "default_fraction_digits BETWEEN 0 AND 10")
@Table(
        name = "currency",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_currency_code", columnNames = "code"),
                @UniqueConstraint(name = "uq_currency_name", columnNames = "name")
        }
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // ← Не "светим наружу" JPA-конструктор
public class CurrencyEntity extends BaseEntity {

    /** Суррогатный PK. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** ISO-4217 код валюты. */
    @Setter(AccessLevel.NONE) // ← Поле нельзя переназначать сеттером
    @NotNull
    @Convert(converter = CurrencyCodeConverter.class)
    @NaturalId() // ← Помечаем поле как натуральный ключ («естественный» неизменяемый ключ)
    @Column(name = "code", length = 3, nullable = false, updatable = false)
    private CurrencyCode code;

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

    /**
     * Специальный конструктор для создания JPA-сущности с заданным натуральным ключом (кодом валюты).
     * Данный конструктор — единственный способ создать JPA-сущность (стандартный конструктор AccessLevel.PROTECTED)
     * и обеспечивает "одноразовость" (сеттер AccessLevel.NONE) установления натурального ключа.
     */
    public CurrencyEntity(CurrencyCode code) {
        this.code = Objects.requireNonNull(code, "code must not be null");
    }
}
