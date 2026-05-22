package com.alligator.market.backend.instrument.application.catalog;

import com.alligator.market.backend.instrument.application.catalog.descriptor.InstrumentCatalogDescriptor;
import com.alligator.market.backend.instrument.application.catalog.exception.InstrumentCatalogDescriptorNotFoundException;
import com.alligator.market.backend.instrument.application.catalog.model.InstrumentCatalogItem;
import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.registry.runtime.RuntimeInstrumentRegistry;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Slf4j
public final class InstrumentCatalogService {
    private static final Comparator<InstrumentCatalogItem> CATALOG_ITEM_ORDER =
            Comparator.comparing(InstrumentCatalogItem::displayName)
                    .thenComparing(item -> item.instrumentCode().value());

    private final RuntimeInstrumentRegistry runtimeInstrumentRegistry;
    private final List<InstrumentCatalogDescriptor> descriptors;

    public InstrumentCatalogService(
            RuntimeInstrumentRegistry runtimeInstrumentRegistry,
            List<InstrumentCatalogDescriptor> descriptors
    ) {
        this.runtimeInstrumentRegistry = Objects.requireNonNull(
                runtimeInstrumentRegistry,
                "runtimeInstrumentRegistry must not be null"
        );
        this.descriptors = List.copyOf(Objects.requireNonNull(
                descriptors,
                "descriptors must not be null"
        ));
    }

    public List<InstrumentCatalogItem> findAll() {
        List<InstrumentCatalogItem> result = runtimeInstrumentRegistry.instrumentsByCode()
                .values()
                .stream()
                .map(this::describe)
                .sorted(CATALOG_ITEM_ORDER)
                .toList();

        log.debug("Found {} instrument catalog items", result.size());
        return result;
    }

    private InstrumentCatalogItem describe(Instrument instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");

        return descriptors.stream()
                .filter(descriptor -> descriptor.supports(instrument))
                .findFirst()
                .orElseThrow(() -> new InstrumentCatalogDescriptorNotFoundException(instrument))
                .describe(instrument);
    }
}
