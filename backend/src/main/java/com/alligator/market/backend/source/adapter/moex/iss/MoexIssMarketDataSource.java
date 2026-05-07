package com.alligator.market.backend.source.adapter.moex.iss;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.source.AbstractMarketDataSource;
import com.alligator.market.domain.source.handler.InstrumentHandler;
import com.alligator.market.domain.source.vo.MarketDataSourceCode;
import com.alligator.market.domain.source.passport.classification.AccessMethod;
import com.alligator.market.domain.source.passport.classification.DeliveryMode;
import com.alligator.market.domain.source.passport.MarketDataSourcePassport;
import com.alligator.market.domain.source.passport.vo.MarketDataSourceDisplayName;
import com.alligator.market.domain.source.policy.MarketDataSourcePolicy;

import java.time.Duration;
import java.util.Set;

/**
 * MOEX ISS runtime market data source adapter.
 */
public final class MoexIssMarketDataSource extends AbstractMarketDataSource<MoexIssMarketDataSource> {

    public static final String SOURCE_CODE_VALUE = "MOEX_ISS";
    public static final MarketDataSourceCode MARKET_DATA_SOURCE_CODE =
            MarketDataSourceCode.of(SOURCE_CODE_VALUE);

    private static final String DISPLAY_NAME = "MOEX Informational & Statistical Server";

    private static final MarketDataSourcePassport PASSPORT = new MarketDataSourcePassport(
            MarketDataSourceDisplayName.of(DISPLAY_NAME),
            DeliveryMode.PULL,
            AccessMethod.API_POLL,
            false
    );

    private static final Duration MIN_UPDATE_INTERVAL = Duration.ofSeconds(1);

    private static final MarketDataSourcePolicy POLICY = new MarketDataSourcePolicy(
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
        super(MARKET_DATA_SOURCE_CODE, PASSPORT, POLICY, handlers);
    }

    @Override
    protected MoexIssMarketDataSource self() {
        return this;
    }
}
