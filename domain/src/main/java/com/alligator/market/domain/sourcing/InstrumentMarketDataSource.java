package com.alligator.market.domain.sourcing;

import com.alligator.market.domain.provider.model.vo.ProviderCode;

import java.util.Objects;

/**
 * Источник рыночных данных для конкретного инструмента.
 *
 * <p>Примечание: На текущем этапе задает только провайдера и признак активности.</p>
 */
public final class InstrumentMarketDataSource {

    /* Код провайдера, который является источником рыночных данных. */
    private final ProviderCode providerCode;

    /* Признак активности источника. */
    private final boolean active;

    public InstrumentMarketDataSource(
            ProviderCode providerCode,
            boolean active
    ) {
        this.providerCode = Objects.requireNonNull(providerCode, "providerCode must not be null");
        this.active = active;
    }

    public ProviderCode providerCode() {
        return providerCode;
    }

    public boolean active() {
        return active;
    }
}
