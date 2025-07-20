package com.alligator.market.backend.provider.catalog.service;

import com.alligator.market.backend.provider.catalog.entity.ProviderCatalogEntity;
import com.alligator.market.backend.provider.catalog.repository.ProviderCatalogRepository;
import com.alligator.market.domain.provider.MarketDataProvider;
import com.alligator.market.domain.provider.ProviderCatalogStatus;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Сервис синхронизации каталога провайдеров с набором бинов-адаптеров.
 * Вызывается при старте приложения и по запросу {@link #refresh()}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProviderCatalogSync {

    private final ProviderCatalogRepository repository;
    private final List<MarketDataProvider> providers;

    /**
     * Синхронизируем каталог после инициализации всех бинов.
     */
    @PostConstruct
    public void init() {
        refresh();
    }

    /**
     * Синхронизировать таблицу provider_catalog с набором адаптеров.
     */
    @Transactional
    public void refresh() {
        log.debug("Start provider catalog sync");

        Map<String, ProviderCatalogEntity> existing = repository.findAll().stream()
                .collect(Collectors.toMap(ProviderCatalogEntity::getProviderCode, Function.identity()));

        for (MarketDataProvider provider : providers) {
            ProviderCatalogEntity entity = existing.remove(provider.providerCode());
            if (entity == null) {
                entity = new ProviderCatalogEntity();
                entity.setProviderCode(provider.providerCode());
            }
            entity.setDisplayName(provider.displayName());
            entity.setInstrumentTypes(provider.instrumentTypes());
            entity.setDeliveryMode(provider.deliveryMode());
            entity.setAccessMethod(provider.accessMethod());
            entity.setSupportsBulkSubscription(provider.supportsBulkSubscription());
            entity.setMinPollPeriodMs((int) provider.minPollPeriodMs().toMillis());
            entity.setStatus(ProviderCatalogStatus.ACTIVE);
            repository.save(entity);
        }

        // Оставшиеся записи помечаем как UNAVAILABLE
        existing.values().forEach(entity -> {
            entity.setStatus(ProviderCatalogStatus.UNAVAILABLE);
            repository.save(entity);
        });

        log.debug("Provider catalog sync completed");
    }
}
