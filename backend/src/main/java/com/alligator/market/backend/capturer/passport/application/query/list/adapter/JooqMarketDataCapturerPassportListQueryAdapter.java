package com.alligator.market.backend.capturer.passport.application.query.list.adapter;

import com.alligator.market.backend.capturer.passport.application.query.list.model.MarketDataCapturerPassportListItem;
import com.alligator.market.backend.capturer.passport.application.query.list.port.MarketDataCapturerPassportListQueryPort;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Objects;

import static com.alligator.market.backend.infra.jooq.generated.tables.CapturerPassport.CAPTURER_PASSPORT;

public final class JooqMarketDataCapturerPassportListQueryAdapter
        implements MarketDataCapturerPassportListQueryPort {
    private final DSLContext dsl;

    public JooqMarketDataCapturerPassportListQueryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public List<MarketDataCapturerPassportListItem> findAll() {
        return dsl.select(
                        CAPTURER_PASSPORT.CAPTURER_CODE,
                        CAPTURER_PASSPORT.DISPLAY_NAME,
                        CAPTURER_PASSPORT.LIFECYCLE_STATUS
                )
                .from(CAPTURER_PASSPORT)
                .orderBy(CAPTURER_PASSPORT.CAPTURER_CODE.asc())
                .fetch(record -> new MarketDataCapturerPassportListItem(
                        record.get(CAPTURER_PASSPORT.CAPTURER_CODE),
                        record.get(CAPTURER_PASSPORT.DISPLAY_NAME),
                        record.get(CAPTURER_PASSPORT.LIFECYCLE_STATUS)
                ));
    }
}
