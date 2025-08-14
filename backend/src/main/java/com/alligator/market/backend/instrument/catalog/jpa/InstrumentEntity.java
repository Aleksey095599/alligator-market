package com.alligator.market.backend.instrument.catalog.jpa;

import com.alligator.market.backend.common.jpa.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(
        name = "instrument",
        uniqueConstraints = @UniqueConstraint(name = "uk_instrument_code", columnNames = "instrument_code")
)
@Inheritance(strategy = InheritanceType.JOINED)
public class InstrumentEntity extends BaseEntity {

    /** Суррогатный PK. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Внутренний код инструмента. */
    @Column(name = "instrument_code", length = 12)
    private String internalCode;


}
