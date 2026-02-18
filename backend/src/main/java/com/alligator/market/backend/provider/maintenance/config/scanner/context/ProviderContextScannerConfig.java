package com.alligator.market.backend.provider.maintenance.config.scanner.context;

import com.alligator.market.backend.provider.maintenance.scanner.context.ProviderPassportRegistryAdapter;
import com.alligator.market.domain.provider.model.MarketDataProvider;
import com.alligator.market.domain.provider.registry.passport.ProviderPassportRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Конфигурация wiring {@link ProviderPassportRegistry}.
 */
@Configuration(proxyBeanMethods = false)
public class ProviderContextScannerConfig {

    public static final String BEAN_PROVIDER_CONTEXT_SCANNER = "providerContextScanner";

    /**
     * Бин адаптера сканера контекста, извлекающий провайдеры из Spring-контекста.
     */
    @Bean(BEAN_PROVIDER_CONTEXT_SCANNER)
    public ProviderPassportRegistry providerContextScanner(List<MarketDataProvider> providers) {
        return new ProviderPassportRegistryAdapter(providers);
    }
}
