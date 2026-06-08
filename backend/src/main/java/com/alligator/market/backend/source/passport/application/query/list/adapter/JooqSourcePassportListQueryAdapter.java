package com.alligator.market.backend.source.passport.application.query.list.adapter;

import com.alligator.market.backend.source.passport.application.query.list.model.SourcePassportListItem;
import com.alligator.market.backend.source.passport.application.query.list.port.SourcePassportListQueryPort;
import com.alligator.market.domain.source.passport.store.SourcePassportRecord;
import org.jooq.DSLContext;
import org.jooq.Field;

import java.util.List;
import java.util.Objects;

import static com.alligator.market.backend.infra.jooq.generated.tables.SourcePassport.SOURCE_PASSPORT;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;

public final class JooqSourcePassportListQueryAdapter implements SourcePassportListQueryPort {
    private static final Field<String> SOURCE_PASSPORT_DESCRIPTION =
            field(name("description"), String.class);
    private static final Field<String> SOURCE_PASSPORT_REGISTRY_STATUS =
            field(name("source_passport", "registry_status"), String.class);

    private final DSLContext dsl;

    public JooqSourcePassportListQueryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public List<SourcePassportListItem> findAll() {
        return dsl.select(
                        SOURCE_PASSPORT.SOURCE_CODE,
                        SOURCE_PASSPORT.DISPLAY_NAME,
                        SOURCE_PASSPORT_DESCRIPTION,
                        SOURCE_PASSPORT_REGISTRY_STATUS
                )
                .from(SOURCE_PASSPORT)
                .orderBy(SOURCE_PASSPORT.SOURCE_CODE.asc())
                .fetch(record -> new SourcePassportListItem(
                        record.get(SOURCE_PASSPORT.SOURCE_CODE),
                        record.get(SOURCE_PASSPORT.DISPLAY_NAME),
                        record.get(SOURCE_PASSPORT_DESCRIPTION),
                        SourcePassportRecord.RegistryStatus.valueOf(record.get(SOURCE_PASSPORT_REGISTRY_STATUS))
                ));
    }
}
