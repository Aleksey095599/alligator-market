package com.alligator.market.domain.source.handler.passport.store.sync;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.source.MarketDataSource;
import com.alligator.market.domain.source.handler.InstrumentHandler;
import com.alligator.market.domain.source.handler.passport.SourceHandlerPassport;
import com.alligator.market.domain.source.handler.passport.store.SourceHandlerPassportKey;
import com.alligator.market.domain.source.handler.passport.store.SourceHandlerPassportRecord;
import com.alligator.market.domain.source.handler.passport.store.SourceHandlerPassportStore;
import com.alligator.market.domain.source.registry.RuntimeSourceRegistry;
import com.alligator.market.domain.source.vo.HandlerCode;
import com.alligator.market.domain.source.vo.SourceCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class SnapshotSourceHandlerPassportStoreSynchronizer
        implements SourceHandlerPassportStoreSynchronizer {
    private final RuntimeSourceRegistry sourceRegistry;
    private final SourceHandlerPassportStore passportStore;

    public SnapshotSourceHandlerPassportStoreSynchronizer(
            RuntimeSourceRegistry sourceRegistry,
            SourceHandlerPassportStore passportStore
    ) {
        this.sourceRegistry = Objects.requireNonNull(
                sourceRegistry,
                "sourceRegistry must not be null"
        );
        this.passportStore = Objects.requireNonNull(
                passportStore,
                "passportStore must not be null"
        );
    }

    @Override
    public void synchronizeStoreFromSourceRegistry() {
        Map<SourceHandlerPassportKey, SourceHandlerPassport> passportSnapshot =
                loadPassportSnapshotFromSourceRegistry();

        retirePassportsMissingFromSnapshot(passportSnapshot.keySet());
        saveRegisteredSnapshotRecords(passportSnapshot);
    }

    private Map<SourceHandlerPassportKey, SourceHandlerPassport> loadPassportSnapshotFromSourceRegistry() {
        Map<SourceCode, MarketDataSource> sourceSnapshot = new LinkedHashMap<>(
                Objects.requireNonNull(
                        sourceRegistry.sourcesByCode(),
                        "sourcesByCode must not be null"
                )
        );

        if (sourceSnapshot.isEmpty()) {
            throw new IllegalStateException("Source registry returned no sources");
        }

        return passportsBySourceHandlerKey(sourceSnapshot);
    }

    private Map<SourceHandlerPassportKey, SourceHandlerPassport> passportsBySourceHandlerKey(
            Map<SourceCode, MarketDataSource> sourceSnapshot
    ) {
        Map<SourceHandlerPassportKey, SourceHandlerPassport> passportsByKey = new LinkedHashMap<>();

        for (Map.Entry<SourceCode, MarketDataSource> sourceEntry : sourceSnapshot.entrySet()) {
            SourceCode sourceCode = Objects.requireNonNull(
                    sourceEntry.getKey(),
                    "sourcesByCode must not contain null keys"
            );
            MarketDataSource source = Objects.requireNonNull(
                    sourceEntry.getValue(),
                    "sourcesByCode must not contain null values"
            );
            Set<? extends InstrumentHandler<? extends MarketDataSource, ? extends Instrument>> handlers =
                    Objects.requireNonNull(
                            source.handlers(),
                            "source.handlers must not be null"
                    );

            for (InstrumentHandler<? extends MarketDataSource, ? extends Instrument> handler : handlers) {
                SourceHandlerPassportKey key = new SourceHandlerPassportKey(
                        sourceCode,
                        requireHandlerCode(handler)
                );
                SourceHandlerPassport passport = requirePassport(handler);

                SourceHandlerPassport previousPassport = passportsByKey.put(key, passport);
                if (previousPassport != null) {
                    throw new IllegalArgumentException(
                            "Duplicate source handler passport detected (sourceCode=" +
                                    sourceCode.value() +
                                    ", handlerCode=" +
                                    key.handlerCode().value() +
                                    ")"
                    );
                }
            }
        }

        if (passportsByKey.isEmpty()) {
            throw new IllegalStateException("Source registry returned no source handlers");
        }

        return Collections.unmodifiableMap(passportsByKey);
    }

    private static HandlerCode requireHandlerCode(
            InstrumentHandler<? extends MarketDataSource, ? extends Instrument> handler
    ) {
        Objects.requireNonNull(handler, "source.handlers must not contain null values");

        return Objects.requireNonNull(
                handler.handlerCode(),
                "source.handlerCode must not be null"
        );
    }

    private static SourceHandlerPassport requirePassport(
            InstrumentHandler<? extends MarketDataSource, ? extends Instrument> handler
    ) {
        SourceHandlerPassport passport = Objects.requireNonNull(
                handler.passport(),
                "source.handler.passport must not be null"
        );
        Objects.requireNonNull(
                passport.deliveryMode(),
                "source.handler.passport.deliveryMode must not be null"
        );
        Objects.requireNonNull(
                passport.accessMethod(),
                "source.handler.passport.accessMethod must not be null"
        );

        return passport;
    }

    private void retirePassportsMissingFromSnapshot(Set<SourceHandlerPassportKey> registeredSourceHandlerPassportKeys) {
        passportStore.retireAllExcept(registeredSourceHandlerPassportKeys);
    }

    private void saveRegisteredSnapshotRecords(
            Map<SourceHandlerPassportKey, SourceHandlerPassport> passportSnapshot
    ) {
        passportStore.save(toRegisteredRecords(passportSnapshot));
    }

    private List<SourceHandlerPassportRecord> toRegisteredRecords(
            Map<SourceHandlerPassportKey, SourceHandlerPassport> passportSnapshot
    ) {
        List<SourceHandlerPassportRecord> records = new ArrayList<>(passportSnapshot.size());
        for (Map.Entry<SourceHandlerPassportKey, SourceHandlerPassport> entry : passportSnapshot.entrySet()) {
            SourceHandlerPassportKey key = entry.getKey();
            records.add(SourceHandlerPassportRecord.registered(
                    key.sourceCode(),
                    key.handlerCode(),
                    entry.getValue()
            ));
        }

        return List.copyOf(records);
    }
}
