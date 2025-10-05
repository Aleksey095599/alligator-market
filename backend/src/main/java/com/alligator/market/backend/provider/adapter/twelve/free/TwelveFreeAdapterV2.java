package com.alligator.market.backend.provider.adapter.twelve.free;

import com.alligator.market.backend.provider.contract.SpringMarketDataProvider;
import com.alligator.market.backend.provider.adapter.twelve.free.config.TwelveFreeConnectionProps;
import com.alligator.market.backend.provider.adapter.twelve.free.handler.forex.spot.TwelveFreeFxSpotHandler;
import com.alligator.market.domain.provider.contract.descriptor.AccessMethod;
import com.alligator.market.domain.provider.contract.descriptor.DeliveryMode;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.contract.policy.ProviderPolicy;
import com.alligator.market.domain.provider.contract.settings.ProviderSettings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Set;

/**
 * Адаптер для провайдера рыночных данных TwelveData (free).
 */
@Component("TWELVE_FREE")
public class TwelveFreeAdapterV2 extends SpringMarketDataProvider<TwelveFreeAdapterV2> {

    /* Технический код провайдера. */
    private static final String PROVIDER_CODE = "TWELVE_FREE";

    /* Отображаемое имя провайдера. */
    private static final String DISPLAY_NAME = "TwelveData Free Plan";

    /* Статический дескриптор провайдера. */
    private static final ProviderDescriptor DESCRIPTOR = new ProviderDescriptor(
            DISPLAY_NAME,
            DeliveryMode.PULL,
            AccessMethod.API_POLL,
            false
    );

    /* Иммутабельные настройки провайдера. */
    private static final ProviderPolicy POLICY = ProviderPolicy.ofSeconds(60);

    /* Заглушка для настраиваемых параметров провайдера. */
    private static final ProviderSettings SETTINGS = ProviderSettings.empty();

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
                PROVIDER_CODE,
                DISPLAY_NAME,
                DESCRIPTOR,
                POLICY,
                SETTINGS,
                Set.of(new TwelveFreeFxSpotHandler(webClient, props))
        );
    }

    @Override
    protected TwelveFreeAdapterV2 self() {
        return this;
    }
}
