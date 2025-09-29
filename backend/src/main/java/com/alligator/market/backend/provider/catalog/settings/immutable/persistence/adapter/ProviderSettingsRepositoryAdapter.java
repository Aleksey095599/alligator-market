package com.alligator.market.backend.provider.catalog.settings.immutable.persistence.adapter;

import com.alligator.market.backend.provider.catalog.settings.immutable.persistence.jpa.SettingsEntity;
import com.alligator.market.backend.provider.catalog.settings.immutable.persistence.jpa.SettingsEntityMapper;
import com.alligator.market.backend.provider.catalog.settings.immutable.persistence.jpa.SettingsJpaRepository;
import com.alligator.market.domain.provider.contract.settings.immutable.ProviderSettings;
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

    private final SettingsJpaRepository jpaRepository;
    private final SettingsEntityMapper mapper;

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
        List<SettingsEntity> entities = settings.stream()
                .map(mapper::toEntity)
                .toList();
        jpaRepository.saveAll(entities);
        jpaRepository.flush();
    }
}
