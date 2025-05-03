package com.alligator.market.backend.currency.entity;

import com.alligator.market.backend.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity для валюты. Optimistic-locking + audit унаследованы из BaseEntity.
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
public class Currency extends BaseEntity {

    /**
     * Трёхбуквенный ISO-код валюты: используется как первичный ключ.
     */
    @Id
    @Pattern(regexp = "^[A-Z]{3}$")
    @Column(length = 3, updatable = false, nullable = false)
    private String code;

    /**
     * Полное название валюты.
     */
    @Column(length = 50, nullable = false)
    @Size(max = 50)
    private String name;

    /**
     * Страна или регион обращения.
     */
    @Column(length = 100, nullable = false)
    @Size(max = 100)
    private String country;

    /**
     * Кол-во знаков после запятой для денежных сумм.
     */
    @Column(nullable = false)
    @Min(0)
    @Max(10)
    private Integer decimal = 2;

}
