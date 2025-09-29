package com.alligator.market.backend.provider.catalog.descriptor.persistence.adapter;

import com.alligator.market.backend.provider.catalog.descriptor.persistence.jpa.DescriptorEntity;
import com.alligator.market.backend.provider.catalog.descriptor.persistence.jpa.DescriptorEntityMapper;
import com.alligator.market.backend.provider.catalog.descriptor.persistence.jpa.DescriptorJpaRepository;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.repository.ProviderDescriptorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.*;

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
        List<DescriptorEntity> entities = jpaRepository.findAll(Sort.by("providerCode"));
        Map<String, ProviderDescriptor> result = new LinkedHashMap<>();
        for (DescriptorEntity e : entities) {
            String key = e.getProviderCode();
            ProviderDescriptor value = mapper.toDomain(e);
            if (result.putIfAbsent(key, value) != null) {
                throw new IllegalStateException("Duplicate provider code: " + key);
            }
        }
        return result;
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAllInBatch();
        jpaRepository.flush();
    }

    @Override
    public void deleteAllByProviderCodes(Collection<String> providerCodes) {
        if (providerCodes == null || providerCodes.isEmpty()) return;
        jpaRepository.deleteAllByProviderCodeIn(providerCodes);
        jpaRepository.flush();
    }

    @Override
    public void insertAll(Map<String, ProviderDescriptor> descriptors) {
        if (descriptors == null || descriptors.isEmpty()) return;
        List<DescriptorEntity> entities = new ArrayList<>(descriptors.size());
        for (Map.Entry<String, ProviderDescriptor> entry : descriptors.entrySet()) {
            String providerCode = entry.getKey();
            ProviderDescriptor descriptor = entry.getValue();
            if (providerCode == null || descriptor == null) {
                throw new IllegalArgumentException("providerCode and descriptor must not be null");
            }
            DescriptorEntity entity = mapper.toEntity(providerCode, descriptor);
            entities.add(entity);
        }
        jpaRepository.saveAll(entities);
        jpaRepository.flush();
    }
}
