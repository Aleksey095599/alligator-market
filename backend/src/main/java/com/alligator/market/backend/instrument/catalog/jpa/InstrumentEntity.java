package com.alligator.market.backend.instrument.catalog.jpa;

import com.alligator.market.backend.common.jpa.BaseEntity;
import com.alligator.market.domain.instrument.model.InstrumentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "instrument",
        uniqueConstraints = @UniqueConstraint(name = "uk_instrument_code", columnNames = "code")
)
@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class InstrumentEntity extends BaseEntity {

    /** Суррогатный PK. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Внутренний код инструмента. */
    @Column(name = "instrument_code", nullable = false, updatable = false, length = 64)
    private String instrumentCode;

    /** Тип финансового инструмента. */
    @Enumerated(EnumType.STRING)
    @Column(name = "instrument_type", nullable = false, updatable = false, length = 32)
    private InstrumentType instrumentType;
}
