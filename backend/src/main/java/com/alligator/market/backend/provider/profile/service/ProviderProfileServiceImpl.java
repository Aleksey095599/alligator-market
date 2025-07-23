package com.alligator.market.backend.provider.profile.service;

import com.alligator.market.backend.provider.profile.entity.ProviderProfileEntity;
import com.alligator.market.backend.provider.profile.repository.ProviderProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;


/**
 * Сервис для работы с профилями провайдеров рыночных данных.
 * Предоставляет базовые операции для получения и сохранения профилей.
 */
@Service
@RequiredArgsConstructor
public class ProviderProfileServiceImpl implements ProviderProfileService {

    private final ProviderProfileRepository repository;

    /** Возвращает все профили провайдеров */
    @Override
    public List<ProviderProfileEntity> findAll() {
        return repository.findAll();
    }

    /** Сохраняет заданную коллекцию профилей */
    @Override
    public void saveAll(Collection<ProviderProfileEntity> entities) {
        repository.saveAll(entities);
    }
}