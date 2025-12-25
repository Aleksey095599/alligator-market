package com.alligator.market.backend.instrument.base.persistence.jpa;

import com.alligator.market.backend.common.persistence.jpa.entity.BaseEntity;
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

import java.util.Objects;

/**
 * Абстрактная родительская JPA-сущность финансового инструмента.
 *
 * <p>Соответствует доменной модели {@link Instrument}.
 */
@Entity
@Table(
        name = "instrument",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_instrument_code", columnNames = "code")
        },
        indexes = {
                @Index(name = "idx_instrument_type", columnList = "type")
        }
)
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // <-- Нельзя создавать вручную через new Entity(): конструктор без аргументов нужен только ORM
public abstract class InstrumentBaseEntity extends BaseEntity {

    /**
     * Суррогатный PK.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Внутренний код инструмента (уникален в контексте приложения).
     */
    @Setter(AccessLevel.NONE) // <-- Поле нельзя переназначать сеттером, задаётся через метод инициализации
    @NotBlank
    @NaturalId() // <-- Помечаем поле как натуральный ключ
    @Column(name = "code", length = 32, nullable = false, updatable = false)
    private String code;

    /**
     * Символ инструмента для отображения в UI.
     */
    @Setter(AccessLevel.NONE) // <-- Поле нельзя переназначать сеттером, задаётся через метод инициализации
    @NotBlank
    @Column(name = "symbol", length = 32, nullable = false, updatable = false)
    private String symbol;

    /**
     * Тип финансового инструмента.
     */
    @Setter(AccessLevel.NONE) // <-- Поле нельзя переназначать сеттером, задаётся через метод инициализации
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 32, nullable = false, updatable = false)
    private InstrumentType type;

    /**
     * Метод однократной инициализация идентичности сущности.
     */
    @SuppressWarnings("SameParameterValue") // <-- Пока не появились иные инструменты кроме FX_SPOT, давим предупреждение типа "SameParameterValue"
    protected final void initIdentity(
            String code,
            String symbol,
            InstrumentType type
    ) {
        // Защита от повторной инициализации
        if (this.code != null || this.symbol != null || this.type != null) {
            throw new IllegalStateException("Instrument identity already initialized");
        }

        // Базовые проверки аргументов
        Objects.requireNonNull(code, "code must not be null");
        Objects.requireNonNull(symbol, "symbol must not be null");
        Objects.requireNonNull(type, "type must not be null");

        // Нормализуем и проверяем строковые переменные
        final String nCode = code.strip();
        final String nSymbol = symbol.strip();
        if (nCode.isEmpty()) throw new IllegalArgumentException("code must not be blank");
        if (nSymbol.isEmpty()) throw new IllegalArgumentException("symbol must not be blank");

        this.code = nCode;
        this.symbol = nSymbol;
        this.type = type;
    }
}
