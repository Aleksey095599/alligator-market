package com.alligator.market.backend.sourceplan.plan.application.query.options.adapter;

import com.alligator.market.backend.sourceplan.plan.application.query.options.model.CapturerOption;
import com.alligator.market.backend.sourceplan.plan.application.query.options.port.CapturerOptionsQueryPort;
import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.capturer.passport.vo.CapturerDisplayName;
import org.jooq.DSLContext;
import org.jooq.Field;

import java.util.List;
import java.util.Objects;

import static com.alligator.market.backend.infra.jooq.generated.tables.CapturerPassport.CAPTURER_PASSPORT;
import static com.alligator.market.domain.capturer.passport.store.CapturerPassportRecord.RegistryStatus.REGISTERED;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;

public final class JooqCapturerOptionsQueryAdapter implements CapturerOptionsQueryPort {
    private static final Field<String> CAPTURER_PASSPORT_REGISTRY_STATUS =
            field(name("capturer_passport", "registry_status"), String.class);

    private final DSLContext dsl;

    public JooqCapturerOptionsQueryAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public List<CapturerOption> findAllCapturers() {
        return dsl.select(
                        CAPTURER_PASSPORT.CAPTURER_CODE,
                        CAPTURER_PASSPORT.DISPLAY_NAME
                )
                .from(CAPTURER_PASSPORT)
                .where(CAPTURER_PASSPORT_REGISTRY_STATUS.eq(REGISTERED.name()))
                .orderBy(CAPTURER_PASSPORT.CAPTURER_CODE.asc())
                .fetch(record -> new CapturerOption(
                        new CapturerCode(record.get(CAPTURER_PASSPORT.CAPTURER_CODE)),
                        new CapturerDisplayName(record.get(CAPTURER_PASSPORT.DISPLAY_NAME))
                ));
    }
}
