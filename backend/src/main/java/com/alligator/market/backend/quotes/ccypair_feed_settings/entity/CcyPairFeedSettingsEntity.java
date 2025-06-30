package com.alligator.market.backend.quotes.ccypair_feed_settings.entity;

import com.alligator.market.backend.common.jpa.entity.BaseEntity;
import com.alligator.market.backend.currency_pairs.entity.Pair;
import com.alligator.market.backend.quotes.providers.list.entity.Provider;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity для таблицы 'ccypair_feed_settings', где хранятся настройки потоков котировок от провайдеров
 * для валютных пар. Синхронизирована с доменной моделью 'CcyPairFeedSettings' через 'CcyPairFeedSettingsMapper'.
 */
@Entity
@Table(
        name = "ccypair_feed_settings",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_ccypair_feed_settings_pair_provider", columnNames = {"pair_id", "provider"})
        }
)
@Getter
@Setter
@NoArgsConstructor
public class CcyPairFeedSettingsEntity extends BaseEntity {

    /* Суррогатный PK */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* Валютная пара (FK на ccypair.id) */
    @ManyToOne(optional = false)
    @JoinColumn(name = "pair_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_feed_settings_pair"))
    private Pair pair;

    /* Провайдер котировок (FK на provider.name) */
    @ManyToOne(optional = false)
    @JoinColumn(name = "provider", referencedColumnName = "name",
            foreignKey = @ForeignKey(name = "fk_feed_settings_provider"))
    private Provider provider;

    /* Приоритет провайдера для пары */
    @Column(nullable = false)
    private short priority;

    /* Период запроса котировок, мс (для PUSH всегда 0) */
    @Column(name = "fetch_period_ms", nullable = false)
    private int fetchPeriodMs;

    /* Признак активности потока */
    @Column(nullable = false)
    private boolean enabled;

}
