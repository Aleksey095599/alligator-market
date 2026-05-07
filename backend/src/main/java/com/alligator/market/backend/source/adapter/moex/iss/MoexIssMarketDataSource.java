package com.alligator.market.backend.source.adapter.moex.iss;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.source.AbstractMarketDataSource;
import com.alligator.market.domain.source.handler.InstrumentHandler;
import com.alligator.market.domain.source.vo.ProviderCode;
import com.alligator.market.domain.source.passport.classification.AccessMethod;
import com.alligator.market.domain.source.passport.classification.DeliveryMode;
import com.alligator.market.domain.source.passport.ProviderPassport;
import com.alligator.market.domain.source.passport.vo.ProviderDisplayName;
import com.alligator.market.domain.source.policy.ProviderPolicy;

import java.time.Duration;
import java.util.Set;

/**
 * MOEX ISS runtime market data source adapter.
 */
public final class MoexIssMarketDataSource extends AbstractMarketDataSource<MoexIssMarketDataSource> {

    public static final String PROVIDER_CODE_VALUE = "MOEX_ISS";
    public static final ProviderCode PROVIDER_CODE = ProviderCode.of(PROVIDER_CODE_VALUE);

    private static final String DISPLAY_NAME = "MOEX Informational & Statistical Server";

    private static final ProviderPassport PASSPORT = new ProviderPassport(
            ProviderDisplayName.of(DISPLAY_NAME),
            DeliveryMode.PULL,
            AccessMethod.API_POLL,
            false
    );

    private static final Duration MIN_UPDATE_INTERVAL = Duration.ofSeconds(1);

    private static final ProviderPolicy POLICY = new ProviderPolicy(
            MIN_UPDATE_INTERVAL
    );

    /**
     * Конструктор адаптера MOEX ISS.
     *
     * <p>Примечание: Spring автоматически инжектит все бины InstrumentHandler, совместимые с
     * MoexIssMarketDataSource для любых Instrument и объединяет их в единый Set.<p/>
     */
    public MoexIssMarketDataSource(
            Set<? extends InstrumentHandler<MoexIssMarketDataSource, ? extends Instrument>> handlers
    ) {
        super(PROVIDER_CODE, PASSPORT, POLICY, handlers);
    }

    @Override
    protected MoexIssMarketDataSource self() {
        return this;
    }
}
