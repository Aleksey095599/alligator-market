package com.alligator.market.backend.quotes.stream.providers.list.entity;

import com.alligator.market.backend.common.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity для провайдера котировок.
 */
@Entity
@Table(
        name = "provider",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_provider_name_mode",
                        columnNames = {"name", "mode"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Provider extends BaseEntity {

    /* Суррогатный PK */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* Имя провайдера */
    @Column(length = 50, nullable = false)
    private String name;

    /* Базовый URL API */
    @Column(name = "base_url", length = 255, nullable = false)
    private String baseUrl;

    /* Режим работы (PULL/PUSH) */
    @Column(length = 4, nullable = false)
    private String mode;

    /* API-ключ */
    @Column(name = "api_key", length = 255, nullable = false)
    private String apiKey;

}
