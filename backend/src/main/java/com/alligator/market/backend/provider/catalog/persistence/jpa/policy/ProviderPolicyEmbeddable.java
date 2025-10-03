package com.alligator.market.backend.provider.catalog.persistence.jpa.policy;

import com.alligator.market.domain.provider.contract.policy.ProviderPolicy;
import com.alligator.market.domain.provider.reconciliation.ProviderSynchronizer;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Positive;

import java.time.Duration;

/**
 * Embeddable JPA-сущность для параметров политики провайдера.
 * Набор полей аналогичен доменной модели {@link ProviderPolicy}.
 * Все поля не обновляемые (updatable = false): логика синхронизации с контекстом приложения использует
 * стратегию delete → insert {@link ProviderSynchronizer}.
 */
public class ProviderPolicyEmbeddable {

    /** Минимальный интервал обновления котировок (независимо от режима доставки)
     * {@link ProviderPolicy#minUpdateInterval()}. */
    @Positive
    @Column(name = "min_update_interval", nullable = false)
    private Duration minUpdateInterval;
}
