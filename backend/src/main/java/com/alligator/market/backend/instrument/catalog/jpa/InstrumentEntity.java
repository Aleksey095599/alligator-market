package com.alligator.market.backend.instrument.catalog.jpa;

import com.alligator.market.backend.common.jpa.BaseEntity;
import com.alligator.market.domain.instrument.model.InstrumentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Базовая entity финансового инструмента.
 */
@Entity
@Table(
        name = "instrument",
        uniqueConstraints = @UniqueConstraint(name = "uk_instrument_code", columnNames = "instrument_code")
)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class InstrumentEntity extends BaseEntity {

    /** Суррогатный PK. */
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Внутренний код инструмента. */
    @NotBlank
    @Column(name = "instrument_code", nullable = false, updatable = true, length = 32)
    private String instrumentCode;

    /** Тип финансового инструмента. */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "instrument_type", nullable = false, updatable = false, length = 32)
    private InstrumentType instrumentType;
}
