package com.alligator.market.backend.common.persistence.jpa.entity;

import com.alligator.market.backend.config.audit.AuditContextHolder;
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
 * Родительский класс для всех сущностей в проекте.
 * Содержит поля для отслеживания изменений (JPA Audit) и управления версиями (Version).
 */
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
@Getter
@Setter
@NoArgsConstructor
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

    @Setter(AccessLevel.NONE) // доступа нет, только callback ниже может заполнять поле
    @Column(updatable = false, nullable = false)
    private String createdVia;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedTimestamp;

    @LastModifiedBy
    @Column(nullable = false)
    private String updatedBy;

    @Setter(AccessLevel.NONE) // доступа нет, только callback ниже может заполнять поле
    @Column(nullable = false)
    private String updatedVia;

    /* JPA-callback: при вставке берем из контекста. */
    @PrePersist
    private void __onCreateAudit() {
        String via = AuditContextHolder.get().via();
        this.createdVia = via;
        this.updatedVia = via;
        onPrePersist();
    }

    /* JPA-callback: при обновлении берем из контекста. */
    @PreUpdate
    private void __onUpdateAudit() {
        this.updatedVia = AuditContextHolder.get().via();
        onPreUpdate();
    }

    /* Хук для PrePersist в наследниках. */
    protected void onPrePersist() {
        // no-op
    }

    /* Хук для PreUpdate в наследниках. */
    protected void onPreUpdate() {
        // no-op
    }
}
