package com.alligator.market.backend.provider.maintenance.context.config;

import com.alligator.market.backend.provider.maintenance.context.scanner.ProviderContextScannerAdapter;
import com.alligator.market.domain.provider.model.MarketDataProvider;
import com.alligator.market.domain.provider.maintenance.context.scanner.ProviderContextScanner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Конфигурация wiring сканера контекста провайдеров.
 */
@Configuration(proxyBeanMethods = false)
public class ProviderMaintenanceContextScannerConfig {

    public static final String BEAN_PROVIDER_CONTEXT_SCANNER = "providerContextScanner";

    /**
     * Адаптер сканера контекста, извлекающий провайдеры из Spring-контекста.
     */
    @Bean(BEAN_PROVIDER_CONTEXT_SCANNER)
    public ProviderContextScanner providerContextScanner(List<MarketDataProvider> providers) {
        return new ProviderContextScannerAdapter(providers);
    }
}
