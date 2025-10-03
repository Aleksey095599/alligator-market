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

import java.util.Objects;

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

    /** Сравниваем сущности по коду и типу. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InstrumentBaseEntity that)) {
            return false;
        }
        return Objects.equals(code, that.code) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, type);
    }
}
