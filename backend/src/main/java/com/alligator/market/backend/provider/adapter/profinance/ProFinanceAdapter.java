package com.alligator.market.backend.provider.adapter.profinance;

import com.alligator.market.backend.provider.adapter.profinance.config.ProFinanceAdapterProps;
import com.alligator.market.backend.provider.adapter.profinance.handler.forex.spot.ProFinanceFxSpotHandler;
import com.alligator.market.backend.provider.contract.SpringMarketDataProvider;
import com.alligator.market.domain.provider.code.ProviderCode;
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
 * Адаптер для провайдера рыночных данных ProFinance (парсинг с сайта).
 */
@Component("PROFINANCE")
public class ProFinanceAdapter extends SpringMarketDataProvider<ProFinanceAdapter> {

    /* Технический код провайдера: UPPERCASE, формат [A-Z0-9_]+. */
    private static final ProviderCode PROVIDER_CODE = ProviderCode.of("PROFINANCE");

    /* Отображаемое имя провайдера. */
    private static final String DISPLAY_NAME = "ProFinance HTML parse";

    /* Статический дескриптор провайдера. */
    private static final ProviderDescriptor DESCRIPTOR = new ProviderDescriptor(
            DISPLAY_NAME,
            DeliveryMode.PULL,
            AccessMethod.API_POLL,
            false // bulk-подписка не нужна
    );

    /* "Политика провайдера": иммутабельные параметры, которые использует бизнес-логика. */
    private static final ProviderPolicy POLICY = ProviderPolicy.ofSeconds(10);

    /* Настройки провайдера: параметры, которые разрешено менять из frontend. */
    private static final ProviderSettings SETTINGS = ProviderSettings.empty(); // <-- Заглушка до востребования

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
                DESCRIPTOR,
                POLICY,
                SETTINGS,
                Set.of(new ProFinanceFxSpotHandler(webClient, props))
        );
    }

    @Override
    protected ProFinanceAdapter self() {
        return this;
    }
}
