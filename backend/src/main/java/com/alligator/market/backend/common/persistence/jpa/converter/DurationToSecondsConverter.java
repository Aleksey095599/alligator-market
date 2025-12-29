package com.alligator.market.backend.common.persistence.jpa.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Duration;

/**
 * JPA-конвертер {@link Duration} ↔ {@link Long} (число секунд) для хранения в БД.
 *
 * <p>Семантика преобразования:</p>
 * <ul>
 *   <li>В БД пишутся целые секунды из {@link Duration#getSeconds()} — дробная часть
 *       (наносекунды) безвозвратно отбрасывается.</li>
 *   <li>Для отрицательных значений действует поведение {@code getSeconds()} (округление вниз):
 *       {@code PT1.9S --> 1}, а {@code PT-1.9S --> -2}.</li>
 *   <li>{@code null} поля сущности --> {@code null} в колонке и наоборот.</li>
 * </ul>
 *
 * <p><b>Требования к схеме БД</b></p>
 * <ul>
 *   <li>Колонка должна иметь тип, совместимый с 64-битным целым (например, {@code BIGINT}).</li>
 *   <li>При необходимости запретить {@code null} на уровне БД используйте {@code NOT NULL}
 *       и/или валидацию Bean Validation (например, {@code @NotNull}).</li>
 * </ul>
 *
 * <p><b>Когда этот конвертер подходит</b></p>
 * <ul>
 *   <li>Нужно хранить длительности с точностью до секунды.</li>
 *   <li>Допустима потеря суб-секундной части.</li>
 * </ul>
 *
 * <p><b>Когда выбрать другой подход</b></p>
 * <ul>
 *   <li>Нужна точность до миллисекунд/наносекунд --> хранить миллисекунды (LONG) или наносекунды (LONG),
 *       либо строковый ISO‑8601 формат {@code PT...} (но это дороже по месту/индексации).</li>
 *   <li>Нужны интервал‑типы конкретной СУБД (например, {@code INTERVAL} в PostgreSQL) --> используйте
 *       специфичный Dialect/тип Hibernate.</li>
 * </ul>
 *
 * <p><b>Пример использования</b></p>
 * <pre>{@code
 *  @Column(name = "ttl_seconds", nullable = false)
 *  @Convert(converter = DurationToSecondsConverter.class)
 *  private Duration ttl;
 * }</pre>
 *
 * <p><b>Потокобезопасность</b>:</p>
 * класс статичен и не имеет состояния, пригоден для повторного использования.</p>
 *
 * @implSpec Реализация намеренно null‑safe, чтобы не ломать сценарии JPA при чтении/записи {@code null}.
 * @since 1.0
 */
@Converter()
public class DurationToSecondsConverter implements AttributeConverter<Duration, Long> {

    /**
     * Преобразует {@link Duration} к целым секундам для записи в БД.
     *
     * @param attribute длительность или {@code null}
     * @return количество секунд ({@link Long}) или {@code null}, если {@code attribute == null}
     */
    @Override
    public Long convertToDatabaseColumn(Duration attribute) {
        return (attribute == null) ? null : attribute.getSeconds();
    }

    /**
     * Восстанавливает {@link Duration} из количества секунд, прочитанных из БД.
     *
     * @param dbData количество секунд ({@link Long}) или {@code null}
     * @return {@link Duration} или {@code null}, если {@code dbData == null}
     */
    @Override
    public Duration convertToEntityAttribute(Long dbData) {
        return (dbData == null) ? null : Duration.ofSeconds(dbData);
    }
}
