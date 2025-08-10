package com.alligator.market.backend.config.audit;

import com.alligator.market.backend.common.jpa.BaseEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

/**
 * JPA Listener: проставляет createdVia/updatedVia из AuditContextHolder.
 */
public class AuditViaListener {

    @PrePersist
    public void prePersist(Object entity) {
        if (entity instanceof BaseEntity e) {
            String via = AuditContextHolder.get().via();
            e.setCreatedVia(via);
            e.setUpdatedVia(via);
        }
    }

    @PreUpdate
    public void preUpdate(Object entity) {
        if (entity instanceof BaseEntity e) {
            e.setUpdatedVia(AuditContextHolder.get().via());
        }
    }
}
