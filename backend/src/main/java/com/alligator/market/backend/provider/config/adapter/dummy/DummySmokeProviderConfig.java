package com.alligator.market.backend.provider.config.adapter.dummy;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.marketdata.tick.level.source.SourceMarketDataTick;
import com.alligator.market.domain.provider.MarketDataProvider;
import com.alligator.market.domain.provider.passport.ProviderPassport;
import com.alligator.market.domain.provider.passport.classification.AccessMethod;
import com.alligator.market.domain.provider.passport.classification.DeliveryMode;
import com.alligator.market.domain.provider.passport.vo.ProviderDisplayName;
import com.alligator.market.domain.provider.policy.ProviderPolicy;
import com.alligator.market.domain.provider.vo.ProviderCode;
import org.reactivestreams.Publisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration(proxyBeanMethods = false)
public class DummySmokeProviderConfig {

    public static final String PROVIDER_CODE_VALUE = "DUMMY_SMOKE_PROVIDER";

    private static final ProviderCode PROVIDER_CODE = ProviderCode.of(PROVIDER_CODE_VALUE);
    private static final ProviderPassport PASSPORT = new ProviderPassport(
            ProviderDisplayName.of("Dummy Smoke Provider"),
            DeliveryMode.PULL,
            AccessMethod.API_POLL,
            false
    );
    private static final ProviderPolicy POLICY = new ProviderPolicy(Duration.ofSeconds(1));

    @Bean("dummySmokeProvider")
    public MarketDataProvider dummySmokeProvider() {
        return new MarketDataProvider() {
            @Override
            public ProviderCode providerCode() {
                return PROVIDER_CODE;
            }

            @Override
            public ProviderPassport passport() {
                return PASSPORT;
            }

            @Override
            public ProviderPolicy policy() {
                return POLICY;
            }

            @Override
            public <I extends Instrument> Publisher<SourceMarketDataTick> quote(I instrument) {
                return subscriber -> subscriber.onError(
                        new UnsupportedOperationException("Dummy provider is used only for projection smoke tests")
                );
            }
        };
    }
}
