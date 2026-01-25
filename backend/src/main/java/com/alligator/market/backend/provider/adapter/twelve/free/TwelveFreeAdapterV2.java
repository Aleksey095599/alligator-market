package com.alligator.market.backend.provider.adapter.twelve.free;

import com.alligator.market.backend.provider.adapter.twelve.free.config.TwelveFreeConnectionProps;
import com.alligator.market.backend.provider.adapter.twelve.free.handler.forex.spot.TwelveFreeFxSpotHandler;
import com.alligator.market.backend.provider.adapter.common.SpringMarketDataProvider;
import com.alligator.market.domain.provider.code.ProviderCode;
import com.alligator.market.domain.provider.contract.passport.AccessMethod;
import com.alligator.market.domain.provider.contract.passport.DeliveryMode;
import com.alligator.market.domain.provider.contract.passport.ProviderPassport;
import com.alligator.market.domain.provider.contract.policy.ProviderPolicy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Set;

// TODO: заменить на заглушку с паспортом
/**
 * Адаптер для провайдера рыночных данных TwelveData (free).
 */
@Component("TWELVE_FREE")
public class TwelveFreeAdapterV2 extends SpringMarketDataProvider<TwelveFreeAdapterV2> {

    /* Технический код провайдера: UPPERCASE, формат [A-Z0-9_]+. */
    private static final ProviderCode PROVIDER_CODE = ProviderCode.of("TWELVE_FREE");

    /* Отображаемое имя провайдера. */
    private static final String DISPLAY_NAME = "TwelveData Free Plan";

    /* Статический паспорт провайдера. */
    private static final ProviderPassport PASSPORT = new ProviderPassport(
            DISPLAY_NAME,
            DeliveryMode.PULL,
            AccessMethod.API_POLL,
            false
    );

    /* Политика провайдера: иммутабельные параметры, которые использует бизнес-логика. */
    private static final ProviderPolicy POLICY = ProviderPolicy.ofSeconds(60);

    /**
     * Конструктор.
     *
     * @param props     параметры подключения к провайдеру
     * @param webClient веб-клиент
     */
    public TwelveFreeAdapterV2(
            TwelveFreeConnectionProps props,
            @Qualifier("twelveFreeWebClient") WebClient webClient
    ) {
        // Конструктор материнского класса провайдера
        super(
                PROVIDER_CODE,
                PASSPORT,
                POLICY,
                Set.of(new TwelveFreeFxSpotHandler(webClient, props))
        );
    }

    @Override
    protected TwelveFreeAdapterV2 self() {
        return this;
    }
}
