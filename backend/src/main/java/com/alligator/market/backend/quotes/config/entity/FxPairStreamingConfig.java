package com.alligator.market.backend.quotes.config.entity;

import com.alligator.market.backend.common.jpa.entity.BaseEntity;
import com.alligator.market.backend.pairs.entity.Pair;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity конфигурации стрима котировок для заданной валютной пары.
 */
@Entity
@Table(
        name = "fx_pair_streaming_cfg",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_fx_pair_streaming_cfg_pair_provider", columnNames = {"pair_id", "provider"})
        }
)
@Getter
@Setter
@NoArgsConstructor
public class FxPairStreamingConfig extends BaseEntity {

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

    /* Приоритет провайдера для пары */
    @Column(nullable = false)
    private short priority;

    /* Частота обновления, мс */
    @Column(name = "refresh_ms", nullable = false)
    private int refreshMs;

    /* Признак активности потока */
    @Column(nullable = false)
    private boolean enabled;

}
