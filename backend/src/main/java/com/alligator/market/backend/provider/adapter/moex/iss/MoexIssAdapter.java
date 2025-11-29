package com.alligator.market.backend.provider.adapter.moex.iss;

import com.alligator.market.backend.provider.adapter.moex.iss.config.MoexIssAdapterProps;
import com.alligator.market.backend.provider.contract.SpringMarketDataProvider;
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
 * <b>Адаптер провайдера рыночных данных MOEX ISS.</b>
 *
 * <p>Адаптер является Spring-компонентом, который инкапсулирует дескриптор, "политику",
 * настройки и обработчики провайдера.</p>
 */
@Component("MOEX_ISS")
public class MoexIssAdapter extends SpringMarketDataProvider<MoexIssAdapter> {

    /* Технический код провайдера: UPPERCASE, формат [A-Z0-9_]+. */
    private static final String PROVIDER_CODE = "MOEX_ISS";

    /* Отображаемое имя провайдера. */
    private static final String DISPLAY_NAME = "MOEX Informational & Statistical Server";

    /* Статический дескриптор провайдера. */
    private static final ProviderDescriptor DESCRIPTOR = new ProviderDescriptor(
            DISPLAY_NAME,
            DeliveryMode.PULL,
            AccessMethod.API_POLL,
            false // <-- bulk-подписка не используется
    );

    /* "Политика провайдера": иммутабельные параметры, которые использует бизнес-логика. */
    private static final ProviderPolicy POLICY = ProviderPolicy.ofSeconds(1); // <-- интервал запросов 1 сек

    /* Настройки провайдера: параметры, которые разрешено менять из frontend. */
    private static final ProviderSettings SETTINGS = ProviderSettings.empty(); // <-- заглушка до востребования

    /**
     * <b>Конструктор адаптера MOEX ISS.</b>
     *
     * <p>Инжектирует параметры подключения и web-клиент провайдера и передаёт их в базовый класс адаптера.</p>
     */
    public MoexIssAdapter(
            MoexIssAdapterProps props,
            @Qualifier("moexIssWebClient") WebClient webClient
    ) {
        // Инициализируем базовый класс адаптера провайдера
        super(
                PROVIDER_CODE,
                DESCRIPTOR,
                POLICY,
                SETTINGS,
                Set.of(new MoexIssFxSpotHandler(webClient, props))
        );
    }

    @Override
    protected MoexIssAdapter self() {
        return this;
    }
}
