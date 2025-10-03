package com.alligator.market.backend.provider.catalog.persistence.jpa.policy;

import com.alligator.market.backend.provider.catalog.persistence.jpa.policy.utility.DurationToSecondsConverter;
import com.alligator.market.domain.provider.contract.policy.ProviderPolicy;
import com.alligator.market.domain.provider.reconciliation.ProviderSynchronizer;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import org.hibernate.validator.constraints.time.DurationMin;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.Objects;

/**
 * Embeddable JPA-сущность для параметров политики провайдера.
 * Набор полей аналогичен доменной модели {@link ProviderPolicy}.
 * Все поля не обновляемые: логика синхронизации с контекстом приложения использует
 * стратегию delete → insert {@link ProviderSynchronizer}.
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProviderPolicyEmbeddable {

    /** Минимальный интервал обновления котировок (независимо от режима доставки)
     * {@link ProviderPolicy#minUpdateInterval()}. */
    @DurationMin(seconds = 1)
    @Convert(converter = DurationToSecondsConverter.class)
    @Column(name = "min_update_interval", nullable = false, updatable = false)
    private Duration minUpdateInterval;

    /** Конструктор. */
    ProviderPolicyEmbeddable(Duration minUpdateInterval) {
        this.minUpdateInterval = Objects.requireNonNull(minUpdateInterval, "minUpdateInterval must not be null");
    }

    /** Фабрика создания иммутабельного embeddable из доменной политики. */
    public static ProviderPolicyEmbeddable from(ProviderPolicy policy) {
        Objects.requireNonNull(policy, "policy must not be null");
        return new ProviderPolicyEmbeddable(policy.minUpdateInterval());
    }
}
