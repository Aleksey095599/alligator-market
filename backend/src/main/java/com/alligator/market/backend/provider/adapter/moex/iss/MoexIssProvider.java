package com.alligator.market.backend.provider.adapter.moex.iss;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.provider.AbstractMarketDataProvider;
import com.alligator.market.domain.provider.handler.InstrumentHandler;
import com.alligator.market.domain.provider.vo.ProviderCode;
import com.alligator.market.domain.provider.passport.classification.AccessMethod;
import com.alligator.market.domain.provider.passport.classification.DeliveryMode;
import com.alligator.market.domain.provider.passport.ProviderPassport;
import com.alligator.market.domain.provider.policy.ProviderPolicy;

import java.time.Duration;
import java.util.Set;

/**
 * Адаптер провайдера рыночных данных MOEX ISS.
 */
public final class MoexIssProvider extends AbstractMarketDataProvider<MoexIssProvider> {

    public static final String PROVIDER_CODE_VALUE = "MOEX_ISS";
    public static final ProviderCode PROVIDER_CODE = ProviderCode.of(PROVIDER_CODE_VALUE);

    private static final String DISPLAY_NAME = "MOEX Informational & Statistical Server";

    private static final ProviderPassport PASSPORT = new ProviderPassport(
            DISPLAY_NAME,
            DeliveryMode.PULL,
            AccessMethod.API_POLL,
            false
    );

    private static final ProviderPolicy POLICY = new ProviderPolicy(
            Duration.ofSeconds(1),
            100
    );

    /**
     * Конструктор адаптера MOEX ISS.
     *
     * <p>Примечание: Spring автоматически инжектит все бины InstrumentHandler, совместимые с MoexIssProvider
     * для любых Instrument и объединяет их в единый Set.<p/>
     */
    public MoexIssProvider(
            Set<? extends InstrumentHandler<MoexIssProvider, ? extends Instrument>> handlers
    ) {
        super(PROVIDER_CODE, PASSPORT, POLICY, handlers);
    }

    @Override
    protected MoexIssProvider self() {
        return this;
    }
}
