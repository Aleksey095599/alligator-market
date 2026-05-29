package com.alligator.market.backend.capturer.passport.application.query.list.adapter;

import com.alligator.market.backend.capturer.passport.application.query.list.model.CapturerPassportListItem;
import com.alligator.market.backend.capturer.passport.application.query.list.port.CapturerPassportListQueryPort;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Objects;

import static com.alligator.market.backend.infra.jooq.generated.tables.CapturerPassport.CAPTURER_PASSPORT;

public final class JooqCapturerPassportListQueryAdapter
        implements CapturerPassportListQueryPort {
    private final DSLContext dsl;

    public JooqCapturerPassportListQueryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public List<CapturerPassportListItem> findAll() {
        return dsl.select(
                        CAPTURER_PASSPORT.CAPTURER_CODE,
                        CAPTURER_PASSPORT.DISPLAY_NAME,
                        CAPTURER_PASSPORT.LIFECYCLE_STATUS
                )
                .from(CAPTURER_PASSPORT)
                .orderBy(CAPTURER_PASSPORT.CAPTURER_CODE.asc())
                .fetch(record -> new CapturerPassportListItem(
                        record.get(CAPTURER_PASSPORT.CAPTURER_CODE),
                        record.get(CAPTURER_PASSPORT.DISPLAY_NAME),
                        record.get(CAPTURER_PASSPORT.LIFECYCLE_STATUS)
                ));
    }
}
