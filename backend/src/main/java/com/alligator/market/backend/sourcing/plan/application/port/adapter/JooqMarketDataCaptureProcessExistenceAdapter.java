package com.alligator.market.backend.sourcing.plan.application.port.adapter;

import com.alligator.market.backend.sourcing.plan.application.port.MarketDataCaptureProcessExistencePort;
import com.alligator.market.domain.marketdata.capture.process.vo.MarketDataCaptureProcessCode;
import org.jooq.DSLContext;

import java.util.Objects;

import static com.alligator.market.backend.common.persistence.projection.ProjectionLifecycleStatus.ACTIVE;
import static com.alligator.market.backend.infra.jooq.generated.tables.CaptureProcessPassport.CAPTURE_PROCESS_PASSPORT;

/**
 * jOOQ-адаптер {@link MarketDataCaptureProcessExistencePort}.
 */
public final class JooqMarketDataCaptureProcessExistenceAdapter implements MarketDataCaptureProcessExistencePort {

    private final DSLContext dsl;

    public JooqMarketDataCaptureProcessExistenceAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public boolean existsByCode(MarketDataCaptureProcessCode captureProcessCode) {
        Objects.requireNonNull(captureProcessCode, "captureProcessCode must not be null");

        return dsl.fetchExists(
                dsl.selectFrom(CAPTURE_PROCESS_PASSPORT)
                        .where(CAPTURE_PROCESS_PASSPORT.CAPTURE_PROCESS_CODE.eq(captureProcessCode.value()))
                        .and(CAPTURE_PROCESS_PASSPORT.LIFECYCLE_STATUS.eq(ACTIVE.name()))
        );
    }
}
