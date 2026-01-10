package com.alligator.market.backend.instrument.type.forex.currency.catalog.persistence.jpa;

import com.alligator.market.backend.common.persistence.jpa.entity.BaseEntity;
import com.alligator.market.domain.instrument.type.forex.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.currency.model.CurrencyCode;
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
 * JPA-сущность валюты.
 *
 * <p>Поля сущности соответствуют доменной модели валюты {@link Currency}.</p>
 */
@Entity
@Check(
        // CHECK: при DDL-генерации создаётся Hibernate; иначе – «живая» спецификация для миграций.
        constraints = "fraction_digits BETWEEN 0 AND 10"
)
@Table(
        name = "currency",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_currency_code", columnNames = "code"),
                @UniqueConstraint(name = "uq_currency_name", columnNames = "name")
        }
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// <-- Нельзя создавать вручную через new Entity(): конструктор без аргументов нужен только ORM
public class CurrencyEntity extends BaseEntity {

    /**
     * Суррогатный PK.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Уникальный код валюты.
     */
    @Setter(AccessLevel.NONE) // <-- Поле нельзя переназначать сеттером, задаётся один раз через конструктор
    @NotNull
    @Convert(converter = CurrencyCodeConverter.class)
    @NaturalId() // <-- Помечаем поле как натуральный ключ
    @Column(
            name = "code", length = 3,
            nullable = false,
            updatable = false
    )
    private CurrencyCode code;

    /**
     * Наименование валюты.
     */
    @NotBlank
    @Column(
            name = "name", length = 50,
            nullable = false)
    private String name;

    /**
     * Страна или регион обращения.
     */
    @NotBlank
    @Column(
            name = "country", length = 100,
            nullable = false
    )
    private String country;

    /**
     * Количество знаков после запятой для денежных сумм.
     */
    @NotNull
    @Min(0)
    @Max(10)
    @Column(
            name = "fraction_digits",
            nullable = false
    )
    private Integer fractionDigits;

    /**
     * Специальный конструктор – единственный безопасный способ создать сущность.
     *
     * <p>Проверяет корректность входных данных и инициализирует поля идентичности сущности.</p>
     */
    public CurrencyEntity(
            CurrencyCode code
    ) {
        Objects.requireNonNull(code, "code must not be null");
        this.code = code;
    }
}
