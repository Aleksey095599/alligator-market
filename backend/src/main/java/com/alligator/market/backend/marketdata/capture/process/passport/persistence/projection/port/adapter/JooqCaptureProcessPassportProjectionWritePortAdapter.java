package com.alligator.market.backend.marketdata.capture.process.passport.persistence.projection.port.adapter;

import com.alligator.market.backend.marketdata.capture.process.passport.application.projection.port.CaptureProcessPassportProjectionWritePort;
import com.alligator.market.domain.marketdata.capture.process.passport.CaptureProcessPassport;
import com.alligator.market.domain.marketdata.capture.process.vo.CaptureProcessCode;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Query;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.alligator.market.backend.infra.jooq.generated.tables.CaptureProcessPassport.CAPTURE_PROCESS_PASSPORT;
import static org.jooq.impl.DSL.excluded;

/**
 * jOOQ-реализация write-порта {@link CaptureProcessPassportProjectionWritePort}.
 */
public class JooqCaptureProcessPassportProjectionWritePortAdapter
        implements CaptureProcessPassportProjectionWritePort {

    /* DSLContext для выполнения SQL-запросов через jOOQ. */
    private final DSLContext dsl;

    public JooqCaptureProcessPassportProjectionWritePortAdapter(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl must not be null");
    }

    @Override
    public void deleteAllExcept(Set<CaptureProcessCode> activeCodes) {
        validateActiveCodes(activeCodes);

        // Преобразуем VO в набор строковых кодов для SQL-предиката NOT IN.
        Set<String> activeValues = new LinkedHashSet<>(activeCodes.size());
        for (CaptureProcessCode code : activeCodes) {
            activeValues.add(code.value());
        }

        dsl.deleteFrom(CAPTURE_PROCESS_PASSPORT)
                .where(CAPTURE_PROCESS_PASSPORT.CAPTURE_PROCESS_CODE.notIn(activeValues))
                .execute();
    }

    @Override
    public void upsertAll(Map<CaptureProcessCode, CaptureProcessPassport> passports) {
        validatePassportsMap(passports);
        if (passports.isEmpty()) {
            return; // Контракт: пустая карта = no-op.
        }

        List<Map.Entry<CaptureProcessCode, CaptureProcessPassport>> entries = toValidatedEntries(passports);
        List<Query> queries = new ArrayList<>(entries.size());

        for (Map.Entry<CaptureProcessCode, CaptureProcessPassport> entry : entries) {
            CaptureProcessCode code = entry.getKey();
            CaptureProcessPassport passport = entry.getValue();

            Condition businessFieldsChanged = CAPTURE_PROCESS_PASSPORT.DISPLAY_NAME
                    .isDistinctFrom(excluded(CAPTURE_PROCESS_PASSPORT.DISPLAY_NAME));

            Query query = dsl.insertInto(CAPTURE_PROCESS_PASSPORT)
                    .set(CAPTURE_PROCESS_PASSPORT.CAPTURE_PROCESS_CODE, code.value())
                    .set(CAPTURE_PROCESS_PASSPORT.DISPLAY_NAME, passport.displayName().value())
                    .onConflict(CAPTURE_PROCESS_PASSPORT.CAPTURE_PROCESS_CODE)
                    .doUpdate()
                    .set(CAPTURE_PROCESS_PASSPORT.DISPLAY_NAME, excluded(CAPTURE_PROCESS_PASSPORT.DISPLAY_NAME))
                    .where(businessFieldsChanged);

            queries.add(query);
        }

        dsl.batch(queries).execute();
    }

    /* Контракт deleteAllExcept: set не null/непустой/без null-элементов. */
    private static void validateActiveCodes(Set<CaptureProcessCode> activeCodes) {
        if (activeCodes == null) {
            throw new IllegalArgumentException("activeCodes must not be null");
        }
        if (activeCodes.isEmpty()) {
            throw new IllegalArgumentException("activeCodes must not be empty");
        }

        for (CaptureProcessCode code : activeCodes) {
            if (code == null) {
                throw new IllegalArgumentException("activeCodes must not contain null");
            }
        }
    }

    /* Контракт upsertAll: map не null. */
    private static void validatePassportsMap(Map<CaptureProcessCode, CaptureProcessPassport> passports) {
        if (passports == null) {
            throw new IllegalArgumentException("passports must not be null");
        }
    }

    /* Контракт upsertAll: map без null-ключей и null-значений. */
    private static List<Map.Entry<CaptureProcessCode, CaptureProcessPassport>> toValidatedEntries(
            Map<CaptureProcessCode, CaptureProcessPassport> passports
    ) {
        List<Map.Entry<CaptureProcessCode, CaptureProcessPassport>> entries = new ArrayList<>(passports.size());
        for (Map.Entry<CaptureProcessCode, CaptureProcessPassport> entry : passports.entrySet()) {
            if (entry.getKey() == null) {
                throw new IllegalArgumentException("passports must not contain null keys");
            }
            if (entry.getValue() == null) {
                throw new IllegalArgumentException("passports must not contain null values");
            }
            entries.add(entry);
        }
        return entries;
    }
}
