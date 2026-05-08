package com.alligator.market.backend.instrument.asset.forex.fxspot.application.usage.port.adapter;

import com.alligator.market.backend.instrument.asset.forex.fxspot.application.usage.contributor.FxSpotUsageContributor;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.usage.port.FxSpotUsageCheckPort;
import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.List;
import java.util.Objects;

public final class FxSpotUsageCheckAdapter implements FxSpotUsageCheckPort {
    private final List<FxSpotUsageContributor> contributors;

    public FxSpotUsageCheckAdapter(List<FxSpotUsageContributor> contributors) {
        Objects.requireNonNull(contributors, "contributors must not be null");

        this.contributors = List.copyOf(contributors);

        for (FxSpotUsageContributor contributor : this.contributors) {
            Objects.requireNonNull(contributor, "contributor must not be null");
        }
    }

    @Override
    public boolean isUsed(InstrumentCode instrumentCode) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

        for (FxSpotUsageContributor contributor : contributors) {
            if (contributor.isUsed(instrumentCode)) {
                return true;
            }
        }

        return false;
    }
}
