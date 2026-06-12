package com.alligator.market.backend.instrument.identity.persistence.jooq;

import com.alligator.market.domain.instrument.identity.InstrumentIdentityStore;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Objects;

import static com.alligator.market.backend.infra.jooq.generated.tables.InstrumentIdentity.INSTRUMENT_IDENTITY;

public final class JooqInstrumentIdentityStoreAdapter implements InstrumentIdentityStore {
    private final DSLContext dsl;

    public JooqInstrumentIdentityStoreAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public boolean contains(InstrumentCode code) {
        Objects.requireNonNull(code, "code must not be null");

        return dsl.fetchExists(
                dsl.selectFrom(INSTRUMENT_IDENTITY)
                        .where(INSTRUMENT_IDENTITY.INSTRUMENT_CODE.eq(code.value()))
        );
    }

    @Override
    public List<InstrumentCode> instrumentCodes() {
        return dsl.select(INSTRUMENT_IDENTITY.INSTRUMENT_CODE)
                .from(INSTRUMENT_IDENTITY)
                .orderBy(INSTRUMENT_IDENTITY.INSTRUMENT_CODE.asc())
                .fetch(INSTRUMENT_IDENTITY.INSTRUMENT_CODE)
                .stream()
                .map(InstrumentCode::new)
                .toList();
    }
}
