package com.alligator.market.backend.provider.catalog.descriptor.persistence.adapter;

import com.alligator.market.backend.provider.catalog.descriptor.persistence.jpa.DescriptorEntity;
import com.alligator.market.backend.provider.catalog.descriptor.persistence.jpa.DescriptorEntityMapper;
import com.alligator.market.backend.provider.catalog.descriptor.persistence.jpa.DescriptorJpaRepository;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.repository.ProviderDescriptorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Адаптер, реализующий порт {@link ProviderDescriptorRepository} через Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class ProviderDescriptorRepositoryAdapter implements ProviderDescriptorRepository {

    private final DescriptorJpaRepository jpaRepository;
    private final DescriptorEntityMapper mapper;

    @Override
    public List<ProviderDescriptor> findAll() {
        return jpaRepository.findAll(Sort.by("providerCode")).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteAll() {
        // Удаляем все записи таблицы без постраничного обхода
        jpaRepository.deleteAllInBatch();
    }

    @Override
    public void saveAll(List<ProviderDescriptor> descriptors) {
        // Готовим список сущностей и сохраняем пакетно через JPA
        List<DescriptorEntity> entities = descriptors.stream()
                .map(mapper::toEntity)
                .toList();
        jpaRepository.saveAll(entities);
    }
}
