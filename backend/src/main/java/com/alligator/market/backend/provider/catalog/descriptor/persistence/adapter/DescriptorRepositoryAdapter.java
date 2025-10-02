package com.alligator.market.backend.provider.catalog.descriptor.persistence.adapter;

import com.alligator.market.backend.provider.catalog.descriptor.persistence.jpa.DescriptorEntity;
import com.alligator.market.backend.provider.catalog.descriptor.persistence.jpa.DescriptorEntityMapper;
import com.alligator.market.backend.provider.catalog.descriptor.persistence.jpa.DescriptorJpaRepository;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.repository.ProviderDescriptorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Адаптер, реализующий доменный порт {@link ProviderDescriptorRepository} через Spring Data JPA.
 */
@Repository
@RequiredArgsConstructor
public class DescriptorRepositoryAdapter implements ProviderDescriptorRepository {

    /* JPA-репозиторий дескрипторов. */
    private final DescriptorJpaRepository jpaRepository;

    /* Маппер сущность ⇄ доменная модель. */
    private final DescriptorEntityMapper mapper;

    @Override
    public Map<String, ProviderDescriptor> findAll() {
        Map<String, ProviderDescriptor> descriptors = new LinkedHashMap<>();
        List<DescriptorEntity> entities = jpaRepository.findAll(Sort.by("providerCode"));
        for (DescriptorEntity entity : entities) {
            descriptors.put(entity.getProviderCode(), mapper.toDomain(entity));
        }
        return descriptors;
    }

    /* Удаляем все дескрипторы одним батчем. */
    @Override
    public void deleteAll() {
        jpaRepository.deleteAllInBatch();
    }

    /** Удаляем дескрипторы по кодам провайдеров одним батчем. */
    @Override
    public void deleteAllByProviderCodes(Set<String> providerCodes) {
        if (providerCodes.isEmpty()) {
            return;
        }
        jpaRepository.deleteAllByProviderCodeIn(providerCodes);
    }

    /** Выполняем пакетную вставку дескрипторов. */
    @Override
    public void insertAll(Map<String, ProviderDescriptor> descriptors) {
        if (descriptors.isEmpty()) {
            return;
        }
        List<DescriptorEntity> entities = new ArrayList<>(descriptors.size());
        for (Map.Entry<String, ProviderDescriptor> entry : descriptors.entrySet()) {
            entities.add(mapper.toEntity(entry.getKey(), entry.getValue()));
        }
        jpaRepository.saveAllAndFlush(entities);
    }
}
