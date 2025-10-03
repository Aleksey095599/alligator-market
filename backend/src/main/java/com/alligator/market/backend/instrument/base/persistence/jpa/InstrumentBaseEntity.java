package com.alligator.market.backend.instrument.base.persistence.jpa;

import com.alligator.market.backend.common.jpa.BaseEntity;
import com.alligator.market.domain.instrument.type.InstrumentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

/**
 * Абстрактная родительская JPA-сущность финансового инструмента.
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
@NoArgsConstructor
public abstract class InstrumentBaseEntity extends BaseEntity {

    /** Суррогатный PK. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Внутренний код инструмента (уникален). */
    @NotBlank
    @NaturalId() // Полезно, так как по сути данное поле это «естественный» неизменяемый ключ
    @Column(name = "code", length = 32, nullable = false, updatable = false)
    private String code;

    /** Тип финансового инструмента. */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 32, nullable = false, updatable = false)
    private InstrumentType type;
}
