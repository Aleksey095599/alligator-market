package com.alligator.market.backend.provider.catalog.service;

import com.alligator.market.backend.provider.catalog.entity.ProviderCatalogEntity;
import com.alligator.market.backend.provider.catalog.repository.ProviderCatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;


/**
 * Сервис для работы с каталогом провайдеров рыночных данных.
 * Предоставляет базовые операции для получения и сохранения записей каталога.
 */
@Service
@RequiredArgsConstructor
public class ProviderCatalogService {

    private final ProviderCatalogRepository repository;

    /**
     * Возвращает все записи каталога провайдеров.
     */
    public List<ProviderCatalogEntity> findAll() {
        return repository.findAll();
    }

    /**
     * Сохраняет заданную коллекцию записей в каталог.
     */
    public void saveAll(Collection<ProviderCatalogEntity> entities) {
        repository.saveAll(entities);
    }
}