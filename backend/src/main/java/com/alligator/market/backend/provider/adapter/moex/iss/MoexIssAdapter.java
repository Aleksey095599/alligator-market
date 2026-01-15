package com.alligator.market.backend.provider.adapter.moex.iss;

import com.alligator.market.backend.provider.adapter.moex.iss.config.MoexIssAdapterProps;
import com.alligator.market.backend.provider.adapter.moex.iss.handler.forex.spot.MoexIssFxSpotHandler;
import com.alligator.market.backend.provider.adapter.common.SpringMarketDataProvider;
import com.alligator.market.domain.provider.code.ProviderCode;
import com.alligator.market.domain.provider.contract.passport.AccessMethod;
import com.alligator.market.domain.provider.contract.passport.DeliveryMode;
import com.alligator.market.domain.provider.contract.passport.ProviderPassport;
import com.alligator.market.domain.provider.contract.policy.ProviderPolicy;
import com.alligator.market.domain.provider.contract.settings.ProviderSettings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Set;

/**
 * Адаптер провайдера рыночных данных MOEX ISS.
 *
 * <p>Адаптер является Spring-компонентом {@link Component}, который инкапсулирует паспорт, политику, настройки и
 * обработчики провайдера.</p>
 */
@Component("MOEX_ISS")
public class MoexIssAdapter extends SpringMarketDataProvider<MoexIssAdapter> {

    /* Код провайдера. */
    public static final ProviderCode PROVIDER_CODE = ProviderCode.of("MOEX_ISS");

    /* Отображаемое имя провайдера. */
    private static final String DISPLAY_NAME = "MOEX Informational & Statistical Server";

    /* Статический паспорт провайдера. */
    private static final ProviderPassport PASSPORT = new ProviderPassport(
            DISPLAY_NAME,
            DeliveryMode.PULL,
            AccessMethod.API_POLL,
            false
    );

    /* Политика провайдера. */
    private static final ProviderPolicy POLICY = ProviderPolicy.ofSeconds(1); // <-- интервал запросов 1 сек

    /* Настройки провайдера. */
    private static final ProviderSettings SETTINGS = ProviderSettings.empty(); // <-- заглушка до востребования

    /**
     * Конструктор адаптера MOEX ISS.
     *
     * <p>Входные параметры {@code props} и {@code webClient} передаются конструктору
     * для сборки обработчика {@link MoexIssFxSpotHandler}.</p>
     *
     * @param props     параметры подключения к провайдеру {@see MoexIssAdapterProps}
     * @param webClient web-клиент, настроенный для данного провайдера {@see MoexIssWebConfig}
     */
    public MoexIssAdapter(
            MoexIssAdapterProps props,
            @Qualifier("moexIssWebClient") WebClient webClient
    ) {
        // Инициализируем базовый класс
        super(
                PROVIDER_CODE,
                PASSPORT,
                POLICY,
                SETTINGS,
                Set.of(new MoexIssFxSpotHandler(props, webClient))
        );
    }

    @Override
    protected MoexIssAdapter self() {
        return this;
    }
}
