package com.alligator.market.backend.common.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

/* Базовый (родительский) класс для всех entity, задающий поля для аудита (отслеживание изменений)
   и управления версиями (оптимистичная блокировка). */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
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

    @LastModifiedDate
    private Instant updatedTimestamp;

    @LastModifiedBy
    private String updatedBy;

}
