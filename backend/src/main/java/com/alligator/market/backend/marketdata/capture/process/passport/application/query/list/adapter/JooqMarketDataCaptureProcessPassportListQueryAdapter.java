package com.alligator.market.backend.marketdata.capture.process.passport.application.query.list.adapter;

import com.alligator.market.backend.marketdata.capture.process.passport.application.query.list.model.MarketDataCaptureProcessPassportListItem;
import com.alligator.market.backend.marketdata.capture.process.passport.application.query.list.port.MarketDataCaptureProcessPassportListQueryPort;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Objects;

import static com.alligator.market.backend.infra.jooq.generated.tables.CaptureProcessPassport.CAPTURE_PROCESS_PASSPORT;

/**
 * jOOQ implementation of {@link MarketDataCaptureProcessPassportListQueryPort}.
 */
public final class JooqMarketDataCaptureProcessPassportListQueryAdapter
        implements MarketDataCaptureProcessPassportListQueryPort {

    private final DSLContext dsl;

    public JooqMarketDataCaptureProcessPassportListQueryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public List<MarketDataCaptureProcessPassportListItem> findAll() {
        return dsl.select(
                        CAPTURE_PROCESS_PASSPORT.CAPTURE_PROCESS_CODE,
                        CAPTURE_PROCESS_PASSPORT.DISPLAY_NAME,
                        CAPTURE_PROCESS_PASSPORT.LIFECYCLE_STATUS
                )
                .from(CAPTURE_PROCESS_PASSPORT)
                .orderBy(CAPTURE_PROCESS_PASSPORT.CAPTURE_PROCESS_CODE.asc())
                .fetch(record -> new MarketDataCaptureProcessPassportListItem(
                        record.get(CAPTURE_PROCESS_PASSPORT.CAPTURE_PROCESS_CODE),
                        record.get(CAPTURE_PROCESS_PASSPORT.DISPLAY_NAME),
                        record.get(CAPTURE_PROCESS_PASSPORT.LIFECYCLE_STATUS)
                ));
    }
}
