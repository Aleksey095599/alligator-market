package com.alligator.market.backend.common.jpa;

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
 * Родительский класс для всех entity в проекте.
 * Содержит поля для отслеживания изменений и управления версиями.
 */
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
@Getter
@Setter
@NoArgsConstructor
public abstract class BaseEntity {

    @Version
    private Long version;

    @CreatedDate
    private Instant createdTimestamp;

    @CreatedBy
    private String createdBy;

    @Setter(AccessLevel.NONE) // пишем только из колбэка ниже
    private String createdVia;

    @LastModifiedDate
    private Instant updatedTimestamp;

    @LastModifiedBy
    private String updatedBy;

    @Setter(AccessLevel.NONE) // пишем только из колбэка ниже
    private String updatedVia;

    /* JPA-callback: при вставке выставляем из контекста. */
    @PrePersist
    protected void __onCreateAudit() {
        String via = AuditContextHolder.get().via();
        this.createdVia = via; // при вставке совпадают
        this.updatedVia = via;
    }

    /* JPA-callback: при апдейте обновляем из контекста. */
    @PreUpdate
    protected void __onUpdateAudit() {
        this.updatedVia = AuditContextHolder.get().via();
    }
}
