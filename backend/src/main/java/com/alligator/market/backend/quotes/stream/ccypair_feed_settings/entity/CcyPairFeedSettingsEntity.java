package com.alligator.market.backend.quotes.stream.ccypair_feed_settings.entity;

import com.alligator.market.backend.ccypairs.entity.Pair;
import com.alligator.market.backend.common.jpa.entity.BaseEntity;
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

    /* Имя провайдера котировок */
    @Column(length = 50, nullable = false)
    private String provider;

    /* Режим стриминга (PULL/PUSH) */
    @Column(length = 4, nullable = false)
    private String mode;

    /* Приоритет провайдера для пары */
    @Column(nullable = false)
    private short priority;

    /* Частота обновления, мс (для PUSH всегда 0) */
    @Column(name = "refresh_ms", nullable = false)
    private int refreshMs;

    /* Признак активности потока */
    @Column(nullable = false)
    private boolean enabled;

}
