package com.alligator.market.backend.provider.adapter.twelve.free;

import com.alligator.market.backend.provider.adapter.twelve.free.config.TwelveFreeConnectionProps;
import com.alligator.market.backend.provider.adapter.twelve.free.config.TwelveFreeWebConfig;
import com.alligator.market.backend.provider.adapter.twelve.free.handler.forex.spot.TwelveFreeFxSpotHandler;
import com.alligator.market.domain.provider.contract.AbstractMarketDataProvider;
import com.alligator.market.domain.provider.contract.descriptor.AccessMethod;
import com.alligator.market.domain.provider.contract.descriptor.DeliveryMode;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.contract.settings.ProviderSettings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Set;

/**
 * Адаптер для провайдера рыночных данных TwelveData (free).
 */
@Component
public class TwelveFreeAdapterV2 extends AbstractMarketDataProvider<TwelveFreeAdapterV2> {

    private static final String PROVIDER_CODE = "TWELVE_FREE";

    /* Статический дескриптор провайдера. */
    private static final ProviderDescriptor DESCRIPTOR = new ProviderDescriptor(
            PROVIDER_CODE,
            "TwelveData Free Plan",
            DeliveryMode.PULL,
            AccessMethod.API_POLL,
            false
    );

    /* Иммутабельные настройки провайдера. */
    private static final ProviderSettings SETTINGS = new ProviderSettings(
            Duration.ofSeconds(60)
    );

    /**
     * Конструктор.
     *
     * @param props     конфигурационные настройки подключения
     * @param webClient веб-клиент
     */
    public TwelveFreeAdapterV2(
            TwelveFreeConnectionProps props,
            @Qualifier("twelveFreeWebClient") WebClient webClient
    ) {
        super(
                DESCRIPTOR,
                SETTINGS,
                Set.of(new TwelveFreeFxSpotHandler(webClient, props))
        );
    }

    @Override
    protected TwelveFreeAdapterV2 self() {
        return this;
    }
}

