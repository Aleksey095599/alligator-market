package com.alligator.market.backend.provider.readmodel.passport.config.store;

import com.alligator.market.backend.provider.readmodel.passport.store.ProviderPassportStoreAdapter;
import com.alligator.market.backend.provider.readmodel.passport.store.write.jdbc.ProviderPassportWriteStoreJdbcAdapter;
import com.alligator.market.domain.provider.readmodel.passport.store.ProviderPassportProjectionStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Конфигурация wiring {@link ProviderPassportProjectionStore}.
 */
@Configuration(proxyBeanMethods = false)
public class ProviderPassportStoreConfig {

    public static final String BEAN_PROVIDER_PASSPORT_STORE_READ = "providerPassportStoreRead";
    public static final String BEAN_PROVIDER_PASSPORT_STORE_WRITE = "providerPassportStoreWrite";
    public static final String BEAN_PROVIDER_PASSPORT_STORE = "providerPassportStore";

    @Bean(BEAN_PROVIDER_PASSPORT_STORE_READ)
    public ProviderPassportProjectionStore.Read providerPassportStoreRead(JdbcTemplate jdbcTemplate) {
        return new ProviderPassportReadStoreJdbcAdapter(jdbcTemplate);
    }

    @Bean(BEAN_PROVIDER_PASSPORT_STORE_WRITE)
    public ProviderPassportProjectionStore.Write providerPassportStoreWrite(JdbcTemplate jdbcTemplate) {
        return new ProviderPassportWriteStoreJdbcAdapter(jdbcTemplate);
    }

    @Bean(BEAN_PROVIDER_PASSPORT_STORE)
    public ProviderPassportStoreAdapter providerPassportStore(
            @Qualifier(BEAN_PROVIDER_PASSPORT_STORE_READ) ProviderPassportProjectionStore.Read read,
            @Qualifier(BEAN_PROVIDER_PASSPORT_STORE_WRITE) ProviderPassportProjectionStore.Write write
    ) {
        return new ProviderPassportStoreAdapter(read, write);
    }
}
