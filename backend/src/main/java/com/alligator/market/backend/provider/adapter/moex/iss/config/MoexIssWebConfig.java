package com.alligator.market.backend.provider.adapter.moex.iss.config;

import com.alligator.market.backend.provider.config.http.ProviderHttpConfigGlobal;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 * Конфигурация web-клиента провайдера рыночных данных MOEX ISS.
 *
 * <p>Использует единый для всех провайдеров HTTP-клиент из {@link ProviderHttpConfigGlobal}
 * и параметры подключения из {@link MoexIssAdapterProperties}.</p>
 */
@Configuration
public class MoexIssWebConfig {

    /* Параметры подключения к провайдеру MOEX ISS. */
    private final MoexIssAdapterProperties props;

    /* Единый HTTP-клиент провайдеров. */
    private final HttpClient httpClient;

    /**
     * Конструктор конфигурации web-клиента MOEX ISS.
     */
    public MoexIssWebConfig(
            MoexIssAdapterProperties props, // <-- инжекция бина с параметрами подключения
            @Qualifier("providerHttpClient") HttpClient httpClient // <-- инжекция бина единого HTTP-клиента для провайдеров

    ) {
        this.httpClient = httpClient;
        this.props = props;
    }


    /**
     * Бин Web-клиента провайдера рыночных данных MOEX ISS.
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
