package com.alligator.market.backend.provider.adapter.moex.iss;

import com.alligator.market.domain.instrument.base.model.Instrument;
import com.alligator.market.domain.marketdata.provider.model.AbstractMarketDataProvider;
import com.alligator.market.domain.marketdata.provider.model.handler.AbstractInstrumentHandler;
import com.alligator.market.domain.marketdata.provider.model.vo.ProviderCode;
import com.alligator.market.domain.marketdata.provider.model.passport.AccessMethod;
import com.alligator.market.domain.marketdata.provider.model.passport.DeliveryMode;
import com.alligator.market.domain.marketdata.provider.model.passport.ProviderPassport;
import com.alligator.market.domain.marketdata.provider.model.policy.ProviderPolicy;

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

    private static final ProviderPolicy POLICY = ProviderPolicy.ofSeconds(1);

    /**
     * Конструктор адаптера MOEX ISS.
     *
     * <p>Примечание: проверки входящих параметров и инвариантов реализованы в {@link AbstractMarketDataProvider}.</p>
     *
     * @param handlers набор обработчиков инструментов
     */
    public MoexIssProvider(
            Set<? extends AbstractInstrumentHandler<MoexIssProvider, ? extends Instrument>> handlers
    ) {
        super(PROVIDER_CODE, PASSPORT, POLICY, handlers);
    }

    @Override
    protected MoexIssProvider self() {
        return this;
    }
}
