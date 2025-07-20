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
 * Сервис синхронизации каталога провайдеров с найденными в Spring-контексте бинами адаптеров провайдеров.
 * Запускается после старта приложения и может вызываться вручную через {@link #refresh()}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProviderCatalogSync {

    private final ProviderCatalogRepository repository;
    private final List<MarketDataProvider> providers;

    /**
     * Выполняет синхронизацию после инициализации всех бинов Spring.
     */
    @PostConstruct
    public void init() {
        refresh();
    }

    /**
     * Актуализирует записи каталога.
     *
     * @implSpec Последовательно:
     * <ol>
     *   <li>загружает существующие записи из БД в Map по коду провайдера</li>
     *   <li>обновляет или создаёт записи для активных провайдеров и помечает их {@link ProviderCatalogStatus#IMPLEMENTED}</li>
     *   <li>оставшиеся в Map записи помечает {@link ProviderCatalogStatus#NOT_IMPLEMENTED}</li>
     * </ol>
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
            entity.setStatus(ProviderCatalogStatus.IMPLEMENTED);
            repository.save(entity);
        }

        existing.values().forEach(entity -> {
            entity.setStatus(ProviderCatalogStatus.NOT_IMPLEMENTED);
            repository.save(entity);
        });

        log.debug("Provider catalog sync completed");
    }
}
