package com.alligator.market.backend.common.persistence.jpa.entity;

import com.alligator.market.backend.config.audit.context.AuditContextHolder;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

/**
 * Родительская JPA-сущность, содержащая поля аудита и версии.
 */
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
@Getter
@NoArgsConstructor
@Access(AccessType.FIELD) // <-- Маппинг по полям: при чтении/записи ORM не вызывает геттеры/сеттеры
public abstract class BaseEntity {

    @Version
    @Column(nullable = false)
    private Long version;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private Instant createdTimestamp;

    @CreatedBy
    @Column(updatable = false, nullable = false)
    private String createdBy;

    @Setter(AccessLevel.NONE) // <-- Поле нельзя переназначать сеттером, только callback
    @Column(updatable = false, nullable = false)
    private String createdVia;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedTimestamp;

    @LastModifiedBy
    @Column(nullable = false)
    private String updatedBy;

    @Setter(AccessLevel.NONE) // <-- Поле нельзя переназначать сеттером, только callback
    @Column(nullable = false)
    private String updatedVia;

    /* (JPA-callback) При вставке: via ставим из AuditContextHolder. */
    @PrePersist
    private void __onCreateAudit() {
        String via = AuditContextHolder.viaOrFallback();
        this.createdVia = via;
        this.updatedVia = via;
        onPrePersist();
    }

    /* (JPA-callback) При обновлении: via ставим из AuditContextHolder. */
    @PreUpdate
    private void __onUpdateAudit() {
        this.updatedVia = AuditContextHolder.viaOrFallback();
        onPreUpdate();
    }

    /* Хуки для наследников. */
    protected void onPrePersist() { /* no-op */ }
    protected void onPreUpdate() { /* no-op */ }
}
