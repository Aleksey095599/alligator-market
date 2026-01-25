package com.alligator.market.backend.provider.adapter.profinance;

import com.alligator.market.backend.provider.adapter.profinance.config.ProFinanceAdapterProps;
import com.alligator.market.backend.provider.adapter.profinance.handler.forex.spot.ProFinanceFxSpotHandler;
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
 * Адаптер для провайдера рыночных данных ProFinance (парсинг с сайта).
 */
@Component("PROFINANCE")
public class ProFinanceAdapter extends SpringMarketDataProvider<ProFinanceAdapter> {

    /* Технический код провайдера: UPPERCASE, формат [A-Z0-9_]+. */
    private static final ProviderCode PROVIDER_CODE = ProviderCode.of("PROFINANCE");

    /* Отображаемое имя провайдера. */
    private static final String DISPLAY_NAME = "ProFinance HTML parse";

    /* Статический паспорт провайдера. */
    private static final ProviderPassport PASSPORT = new ProviderPassport(
            DISPLAY_NAME,
            DeliveryMode.PULL,
            AccessMethod.API_POLL,
            false // bulk-подписка не нужна
    );

    /* Политика провайдера: иммутабельные параметры, которые использует бизнес-логика. */
    private static final ProviderPolicy POLICY = ProviderPolicy.ofSeconds(10);

    /**
     * Конструктор.
     */
    public ProFinanceAdapter(
            ProFinanceAdapterProps props,
            @Qualifier("proFinanceWebClient") WebClient webClient
    ) {
        // Конструктор материнского класса провайдера
        super(
                PROVIDER_CODE,
                PASSPORT,
                POLICY,
                Set.of(new ProFinanceFxSpotHandler(webClient, props))
        );
    }

    @Override
    protected ProFinanceAdapter self() {
        return this;
    }
}
