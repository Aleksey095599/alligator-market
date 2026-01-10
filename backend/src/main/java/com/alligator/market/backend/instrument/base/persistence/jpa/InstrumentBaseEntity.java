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
 * <p>Ключевые особенности:</p>
 * <ul>
 *     <li>Поля сущности соответствуют доменному контракту {@link Instrument}.</li>
 *     <li>{@link Inheritance}: используется стратегия {@code JOINED}, при которой общие поля хранятся в базовой
 *     таблице, а специфичные — в таблицах наследников.</li>
 *     <li>{@link NoArgsConstructor} с {@code PROTECTED}: конструктор без аргументов нужент только для ORM;
 *     вручную сущность создается через метод однократной инициализации.</li>
 *     <li>Остальные аннотации очевидны.</li>
 * </ul>
 */
@Entity
@Table(
        name = "instrument",
        // Поле задает натуральный ключ
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_instrument_code", columnNames = "code")
        },
        // Индекс по типу инструмента полезен для быстрого поиска
        indexes = {
                @Index(name = "idx_instrument_type", columnList = "type")
        }
)
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
     *
     * <p>Поле является натуральным ключом, поэтому {@code updatable=false} и
     * запрет на переназначение через сеттер {@code @Setter(AccessLevel.NONE)}.</p>
     */
    @Setter(AccessLevel.NONE)
    @NotBlank
    @NaturalId()
    @Column(
            name = "code", length = 32,
            nullable = false,
            updatable = false
    )
    private String code;

    /**
     * Символ инструмента для отображения в UI.
     *
     * <p>Поле задает бизнес-уникльность инструмента, поэтому {@code updatable=false} и
     * запрет на переназначение через сеттер {@code @Setter(AccessLevel.NONE)}.</p>
     */
    @Setter(AccessLevel.NONE)
    @NotBlank
    @Column(
            name = "symbol", length = 32,
            nullable = false,
            updatable = false
    )
    private String symbol;

    /**
     * Тип финансового инструмента.
     *
     * <p>Поле задает бизнес-уникльность инструмента, поэтому {@code updatable=false} и
     * запрет на переназначение через сеттер {@code @Setter(AccessLevel.NONE)}.</p>
     */
    @Setter(AccessLevel.NONE)
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(
            name = "type", length = 32,
            nullable = false,
            updatable = false
    )
    private InstrumentType type;

    /**
     * Метод однократной инициализация сущности.
     */
    // Пока не появились иные инструменты кроме FX_SPOT, давим предупреждение типа "SameParameterValue"
    @SuppressWarnings("SameParameterValue")
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
