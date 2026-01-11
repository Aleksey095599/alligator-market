package com.alligator.market.backend.provider.catalog.persistence.jpa.policy;

import com.alligator.market.backend.common.persistence.jpa.converter.DurationToSecondsConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.time.DurationMin;

import java.time.Duration;

/**
 * Embeddable JPA-сущность политики провайдера.
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProviderPolicyEmbeddable {

    /**
     * Минимальный интервал обновления котировок для любых режимов доставки.
     */
    @NotNull
    @DurationMin(seconds = 1)
    @Convert(converter = DurationToSecondsConverter.class)
    @Column(name = "min_update_interval_seconds", nullable = false, updatable = false)
    private Duration minUpdateInterval;
}
