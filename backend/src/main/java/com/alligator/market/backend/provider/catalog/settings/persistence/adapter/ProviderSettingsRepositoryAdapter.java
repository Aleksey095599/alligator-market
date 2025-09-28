package com.alligator.market.backend.provider.catalog.settings.persistence.adapter;

import com.alligator.market.backend.provider.catalog.settings.persistence.jpa.ProviderSettingsEntity;
import com.alligator.market.backend.provider.catalog.settings.persistence.jpa.ProviderSettingsEntityMapper;
import com.alligator.market.backend.provider.catalog.settings.persistence.jpa.ProviderSettingsJpaRepository;
import com.alligator.market.domain.provider.contract.settings.ProviderSettings;
import com.alligator.market.domain.provider.repository.ProviderSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Адаптер, реализующий порт доменного репозитория для настроек через Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class ProviderSettingsRepositoryAdapter implements ProviderSettingsRepository {

    private final ProviderSettingsJpaRepository jpaRepository;
    private final ProviderSettingsEntityMapper mapper;

    @Override
    public List<ProviderSettings> findAll() {
        return jpaRepository.findAll(Sort.by("providerCode")).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAllInBatch();
    }

    @Override
    public void deleteAllByProviderCodes(Collection<String> providerCodes) {
        if (providerCodes.isEmpty()) return; // Нечего удалять
        jpaRepository.deleteAllByProviderCodeIn(providerCodes);
        jpaRepository.flush(); // важно для порядка операций
    }

    @Override
    public void insertAll(List<ProviderSettings> settings) {
        if (settings.isEmpty()) return; // Нечего вставлять
        List<ProviderSettingsEntity> entities = settings.stream()
                .map(mapper::toEntity)
                .toList();
        jpaRepository.saveAll(entities);
        jpaRepository.flush();
    }
}
