package com.alligator.market.backend.capturer.passport.application.query.list.adapter;

import com.alligator.market.backend.capturer.passport.application.query.list.model.CapturerPassportListItem;
import com.alligator.market.backend.capturer.passport.application.query.list.port.CapturerPassportListQueryPort;
import com.alligator.market.domain.capturer.passport.registry.stored.StoredCapturerPassport;
import org.jooq.DSLContext;
import org.jooq.Field;

import java.util.List;
import java.util.Objects;

import static com.alligator.market.backend.infra.jooq.generated.tables.CapturerPassport.CAPTURER_PASSPORT;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;

public final class JooqCapturerPassportListQueryAdapter
        implements CapturerPassportListQueryPort {
    private static final Field<String> CAPTURER_PASSPORT_DESCRIPTION =
            field(name("description"), String.class);
    private static final Field<String> CAPTURER_PASSPORT_REGISTRY_STATUS =
            field(name("capturer_passport", "registry_status"), String.class);

    private final DSLContext dsl;

    public JooqCapturerPassportListQueryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public List<CapturerPassportListItem> findAll() {
        return dsl.select(
                        CAPTURER_PASSPORT.CAPTURER_CODE,
                        CAPTURER_PASSPORT.DISPLAY_NAME,
                        CAPTURER_PASSPORT_DESCRIPTION,
                        CAPTURER_PASSPORT_REGISTRY_STATUS
                )
                .from(CAPTURER_PASSPORT)
                .orderBy(CAPTURER_PASSPORT.CAPTURER_CODE.asc())
                .fetch(record -> new CapturerPassportListItem(
                        record.get(CAPTURER_PASSPORT.CAPTURER_CODE),
                        record.get(CAPTURER_PASSPORT.DISPLAY_NAME),
                        record.get(CAPTURER_PASSPORT_DESCRIPTION),
                        StoredCapturerPassport.RegistryStatus.valueOf(
                                record.get(CAPTURER_PASSPORT_REGISTRY_STATUS)
                        )
                ));
    }
}
