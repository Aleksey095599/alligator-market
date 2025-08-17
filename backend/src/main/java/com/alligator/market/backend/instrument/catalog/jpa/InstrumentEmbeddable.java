package com.alligator.market.backend.instrument.catalog.jpa;

import com.alligator.market.domain.instrument.model.InstrumentType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Встраиваемая модель общих атрибутов инструмента.
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class InstrumentEmbeddable {

    /** Внутренний код инструмента. */
    @NotBlank
    @Column(length = 32, nullable = false, updatable = false)
    private String code;

    /** Тип финансового инструмента. */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 32, nullable = false, updatable = false)
    private InstrumentType type;
}
