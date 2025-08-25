package com.alligator.market.backend.instrument.catalog.persistence.jpa;

import com.alligator.market.backend.common.jpa.BaseEntity;
import com.alligator.market.domain.instrument.type.InstrumentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Абстрактная сущность базового финансового инструмента.
 * Родитель для сущностей финансовых инструментов всех типов.
 * Не может быть создана напрямую.
 */
@Entity
@Table(name = "instrument")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public abstract class InstrumentEntity extends BaseEntity {

    /** Суррогатный PK. */
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Внутренний код инструмента (уникален). */
    @NotBlank
    @Column(name = "code", length = 32, nullable = false, updatable = false, unique = true)
    private String code;

    /** Тип финансового инструмента. */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 32, nullable = false, updatable = false)
    private InstrumentType type;
}
