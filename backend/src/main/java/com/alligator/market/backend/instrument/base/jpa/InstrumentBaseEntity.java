package com.alligator.market.backend.instrument.base.jpa;

import com.alligator.market.backend.common.jpa.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Абстрактная родительская сущность финансового инструмента.
 */
@Entity
@Table(name = "instrument")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
public abstract class InstrumentBaseEntity extends BaseEntity {

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

    /** Класс финансового инструмента. */
    @NotBlank
    @Column(name = "instrument_class", length = 200, nullable = false, updatable = false)
    private String instrumentClass;
}
