package com.alligator.market.backend.sourceplan.plan.application.query.common.model;

import java.util.List;
import java.util.Objects;

/**
 * Read-модель одного source plan для административного UI.
 *
 * <p>Список sources полный и может включать retired строки.</p>
 */
public record SourcePlanQueryItem(
        String capturerCode,
        String instrumentCode,
        List<MarketDataSourceQueryItem> sources
) {

    public SourcePlanQueryItem {
        Objects.requireNonNull(capturerCode, "capturerCode must not be null");
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(sources, "sources must not be null");
        sources = List.copyOf(sources);
    }
}
