package com.alligator.market.backend.fx.pairs.entity;

import com.alligator.market.backend.common.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/* Entity для валютной пары. */
@Entity
@Table(
        name = "ccypair",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_currency_pair", columnNames = "pair")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Pair extends BaseEntity {

    /* Суррогатный PK */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ISO-4217 код валюты-1 */
    @Pattern(regexp = "^[A-Z]{3}$")
    @Column(length = 3, nullable = false)
    private String code1;

    /* ISO-4217 код валюты-2 */
    @Pattern(regexp = "^[A-Z]{3}$")
    @Column(length = 3, nullable = false)
    private String code2;

    /* Валютная пара как code1 + code2 */
    @Pattern(regexp = "^[A-Z]{6}$")
    @Column(length = 6, nullable = false)
    private String pair;

    /* Кол-во знаков после запятой для курса */
    @Column(nullable = false)
    @Min(0)
    @Max(10)
    private Integer decimal;

}