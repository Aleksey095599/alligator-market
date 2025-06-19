package com.alligator.market.backend.quotes.stream.settings.entity;

import com.alligator.market.backend.common.jpa.entity.BaseEntity;
import com.alligator.market.backend.pairs.entity.Pair;
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
        name = "fx_pair_streaming_cfg",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_fx_pair_streaming_cfg_pair_provider_mode", columnNames = {"pair_id", "provider", "mode"})
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
            foreignKey = @ForeignKey(name = "fk_stream_cfg_pair"))
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
