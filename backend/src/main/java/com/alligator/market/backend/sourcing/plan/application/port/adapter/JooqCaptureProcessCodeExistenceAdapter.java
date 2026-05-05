package com.alligator.market.backend.sourcing.plan.application.port.adapter;

import com.alligator.market.backend.sourcing.plan.application.port.CaptureProcessCodeExistencePort;
import com.alligator.market.domain.marketdata.capture.process.vo.CaptureProcessCode;
import org.jooq.DSLContext;

import java.util.Objects;

import static com.alligator.market.backend.common.persistence.projection.ProjectionLifecycleStatus.ACTIVE;
import static com.alligator.market.backend.infra.jooq.generated.tables.CaptureProcessPassport.CAPTURE_PROCESS_PASSPORT;

/**
 * jOOQ-адаптер {@link CaptureProcessCodeExistencePort}.
 */
public final class JooqCaptureProcessCodeExistenceAdapter implements CaptureProcessCodeExistencePort {

    private final DSLContext dsl;

    public JooqCaptureProcessCodeExistenceAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public boolean existsByCode(CaptureProcessCode captureProcessCode) {
        Objects.requireNonNull(captureProcessCode, "captureProcessCode must not be null");

        return dsl.fetchExists(
                dsl.selectFrom(CAPTURE_PROCESS_PASSPORT)
                        .where(CAPTURE_PROCESS_PASSPORT.CAPTURE_PROCESS_CODE.eq(captureProcessCode.value()))
                        .and(CAPTURE_PROCESS_PASSPORT.LIFECYCLE_STATUS.eq(ACTIVE.name()))
        );
    }
}
