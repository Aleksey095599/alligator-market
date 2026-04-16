package com.alligator.market.backend.instrument.asset.forex.reference.currency.persistence.jpa;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.persistence.jpa.converter.CurrencyCodeConverter;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.Currency;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;
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
import org.hibernate.annotations.Checks;
import org.hibernate.annotations.NaturalId;

import java.util.Objects;

/**
 * JPA-сущность валюты.
 *
 * <p>Назначение: хранение и представление в базе данных валюты; все поля сущности соответствуют доменной модели
 * валюты {@link Currency}.</p>
 *
 * <p>Пояснение некоторых аннотаций:</p>
 * <ul>
 *     <li>{@link NoArgsConstructor} с {@code PROTECTED}: конструктор без аргументов нужен только для ORM;
 *     вручную создаем сущность через специализированный конструктор.</li>
 * </ul>
 */
@Entity
@Checks({
        @Check(
                name = "chk_currency_code_pattern",
                constraints = "code ~ '" + CurrencyCode.PATTERN + "'"
        ),
        @Check(
                name = "chk_currency_fraction_digits",
                constraints = "fraction_digits BETWEEN 0 AND 10"
        )
})
@Table(
        name = "currency",
        uniqueConstraints = {
                // Уникальность кода валюты
                @UniqueConstraint(name = "uq_currency_code", columnNames = "code"),
                // Уникальность наименования валюты
                @UniqueConstraint(name = "uq_currency_name", columnNames = "name")
        }
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CurrencyEntity {

    /**
     * Суррогатный PK.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Код валюты (натуральный ключ).
     *
     * <p>Поле является натуральным ключом, поэтому {@code updatable=false} и
     * запрет на переназначение через сеттер {@code @Setter(AccessLevel.NONE)}.</p>
     */
    @Setter(AccessLevel.NONE)
    @NotNull
    @Convert(converter = CurrencyCodeConverter.class)
    @NaturalId()
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
            nullable = false
    )
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
     * Количество знаков после запятой для денежных сумм в данной валюте.
     *
     * <p>На уровне кода задан дефолт {@code 2} – наиболее распространенное значение на практике.
     * Рекомендуется закрепить {@code DEFAULT 2} в БД миграцией.</p>
     */
    @NotNull
    @Min(0)
    @Max(10)
    @Column(
            name = "fraction_digits",
            nullable = false
    )
    private Integer fractionDigits = 2;

    /**
     * Специальный конструктор – единственный безопасный способ создать сущность.
     *
     * <p>Проверяет входные данные и фиксирует неизменяемые поля сущности.</p>
     */
    public CurrencyEntity(
            CurrencyCode code
    ) {
        Objects.requireNonNull(code, "code must not be null");
        this.code = code;
    }
}
