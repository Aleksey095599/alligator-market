package com.alligator.market.backend.provider.catalog.descriptor.persistence.adapter;

import com.alligator.market.backend.provider.catalog.descriptor.persistence.jpa.DescriptorEntity;
import com.alligator.market.backend.provider.catalog.descriptor.persistence.jpa.DescriptorEntityMapper;
import com.alligator.market.backend.provider.catalog.descriptor.persistence.jpa.DescriptorJpaRepository;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.repository.ProviderDescriptorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Адаптер, реализующий порт доменного репозитория для дескрипторов через Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class ProviderDescriptorRepositoryAdapter implements ProviderDescriptorRepository {

    private final DescriptorJpaRepository jpaRepository;
    private final DescriptorEntityMapper mapper;

    @Override
    public Map<String, ProviderDescriptor> findAll() {
        return jpaRepository.findAll(Sort.by("providerCode")).stream()
                .collect(Collectors.toMap(
                        DescriptorEntity::getProviderCode,
                        mapper::toDomain,
                        (left, right) -> {
                            throw new IllegalStateException("Duplicate provider code found in repository");
                        },
                        LinkedHashMap::new
                ));
    }

    @Override
    public void deleteAll() {
        // Удаляем все записи таблицы без постраничного обхода
        jpaRepository.deleteAllInBatch();
    }

    @Override
    public void deleteAllByProviderCodes(Collection<String> providerCodes) {
        if (providerCodes.isEmpty()) return; // Нечего удалять
        jpaRepository.deleteAllByProviderCodeIn(providerCodes);
        jpaRepository.flush(); // важно для порядка операций
    }

    @Override
    public void insertAll(Map<String, ProviderDescriptor> descriptors) {
        if (descriptors.isEmpty()) return; // Нечего вставлять
        List<DescriptorEntity> entities = descriptors.entrySet().stream()
                .map(e -> mapper.toEntity(e.getKey(), e.getValue()))
                .toList();
        jpaRepository.saveAll(entities);
        jpaRepository.flush();
    }
}
