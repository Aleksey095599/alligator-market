package com.alligator.market.backend.currency.entity;

import com.alligator.market.backend.common.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/* Entity для валюты. */
@Entity
@Table(
        name = "currency",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_currency_code", columnNames = "code"),
                @UniqueConstraint(name = "uq_currency_name", columnNames = "name"),
                @UniqueConstraint(name = "uq_currency_country", columnNames = "country")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Currency extends BaseEntity {

    /* Суррогатный PK */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ISO-4217 код валюты */
    @Pattern(regexp = "^[A-Z]{3}$")
    @Column(length = 3, nullable = false)
    private String code;

    /* Наименование валюты */
    @Column(length = 50, nullable = false)
    private String name;

    /* Страна или регион обращения */
    @Column(length = 100, nullable = false)
    private String country;

    /* Кол-во знаков после запятой для денежных сумм */
    @Column(nullable = false)
    @Min(0)
    @Max(10)
    private Integer decimal;

}