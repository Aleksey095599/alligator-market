package com.alligator.market.backend.instrument.base.persistence.jpa;

import com.alligator.market.backend.common.jpa.BaseEntity;
import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.instrument.type.InstrumentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

/**
 * Абстрактная родительская JPA-сущность финансового инструмента.
 * Соответствует доменной модели {@link Instrument}.
 */
@Entity
@Table(
        name = "instrument",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_instrument_code", columnNames = "code")
        }
)
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // ← скрываем JPA-конструктор
public abstract class InstrumentBaseEntity extends BaseEntity {

    /** Суррогатный PK. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Внутренний код инструмента (уникален в контексте приложения). */
    @Setter(AccessLevel.NONE) // ← Поле нельзя переназначать сеттером
    @NotBlank
    @NaturalId() // ← Помечаем поле как натуральный ключ
    @Column(name = "code", length = 32, nullable = false, updatable = false)
    private String code;

    /** Символ инструмента для отображения в UI. */
    @Setter(AccessLevel.NONE) // ← Поле нельзя переназначать сеттером
    @NotBlank
    @Column(name = "symbol", length = 32, nullable = false, updatable = false)
    private String symbol;

    /** Тип финансового инструмента. */
    @Setter(AccessLevel.NONE) // ← Поле нельзя переназначать сеттером
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 32, nullable = false, updatable = false)
    private InstrumentType type;
}
