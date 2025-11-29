package com.alligator.market.backend.provider.adapter.moex.iss.config;

import com.alligator.market.backend.provider.config.http.ProviderHttpConfigGlobal;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 * <b>Конфигурация web-клиента провайдера рыночных данных MOEX ISS.</b>
 *
 * <p>Использует единый для всех провайдеров HTTP-клиент из {@link ProviderHttpConfigGlobal}
 * и параметры подключения из {@link MoexIssAdapterProps}.</p>
 */
@Configuration
public class MoexIssWebConfig {

    /* Параметры подключения к провайдеру MOEX ISS. */
    private final MoexIssAdapterProps props;

    /* Единый HTTP-клиент провайдеров. */
    private final HttpClient httpClient;

    /**
     * <b>Конструктор конфигурации web-клиента MOEX ISS.</b>
     *
     * <p>Инжектирует параметры подключения и единый HTTP-клиент провайдеров.</p>
     */
    public MoexIssWebConfig(
            MoexIssAdapterProps props, // <-- инжекция бина с параметрами подключения
            @Qualifier("providerHttpClient") HttpClient httpClient // <-- инжекция единого бина HTTP-клиента для провайдеров

    ) {
        this.httpClient = httpClient;
        this.props = props;
    }


    /**
     * <b>Web-клиент провайдера рыночных данных MOEX ISS.</b>
     *
     * <p>Создаётся как Spring-бин на базе общего HTTP-клиента и параметров подключения провайдера.</p>
     */
    @Bean("moexIssWebClient")
    public WebClient moexIssWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(props.baseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader("User-Agent", "Alligator Market")
                .build();
    }
}
