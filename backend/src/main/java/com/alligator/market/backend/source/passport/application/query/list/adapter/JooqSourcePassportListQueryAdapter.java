package com.alligator.market.backend.source.passport.application.query.list.adapter;

import com.alligator.market.backend.source.passport.application.query.list.model.SourcePassportListItem;
import com.alligator.market.backend.source.passport.application.query.list.port.SourcePassportListQueryPort;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Objects;

import static com.alligator.market.backend.infra.jooq.generated.tables.SourcePassport.SOURCE_PASSPORT;

public final class JooqSourcePassportListQueryAdapter implements SourcePassportListQueryPort {
    private final DSLContext dsl;

    public JooqSourcePassportListQueryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public List<SourcePassportListItem> findAll() {
        return dsl.select(
                        SOURCE_PASSPORT.SOURCE_CODE,
                        SOURCE_PASSPORT.DISPLAY_NAME,
                        SOURCE_PASSPORT.LIFECYCLE_STATUS
                )
                .from(SOURCE_PASSPORT)
                .orderBy(SOURCE_PASSPORT.SOURCE_CODE.asc())
                .fetch(record -> new SourcePassportListItem(
                        record.get(SOURCE_PASSPORT.SOURCE_CODE),
                        record.get(SOURCE_PASSPORT.DISPLAY_NAME),
                        record.get(SOURCE_PASSPORT.LIFECYCLE_STATUS)
                ));
    }
}
