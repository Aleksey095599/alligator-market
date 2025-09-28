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
        var result = new LinkedHashMap<String, ProviderDescriptor>();
        for (var entity : jpaRepository.findAll(Sort.by("providerCode"))) {
            var key = entity.getProviderCode();
            var value = mapper.toDomain(entity);
            if (result.putIfAbsent(key, value) != null) { // Если вдруг дубликат по ключу
                throw new IllegalStateException("Duplicate provider code: " + key);
            }
        }
        return result;
    }

    @Override
    public void deleteAll() {
        // Удаляем все записи таблицы без постраничного обхода
        jpaRepository.deleteAllInBatch();
        jpaRepository.flush();
    }

    @Override
    public void deleteAllByProviderCodes(Collection<String> providerCodes) {
        if (providerCodes == null || providerCodes.isEmpty()) return; // Нечего удалять
        jpaRepository.deleteAllByProviderCodeIn(providerCodes);
        jpaRepository.flush(); // важно для порядка операций
    }

    @Override
    public void insertAll(Map<String, ProviderDescriptor> descriptors) {
        if (descriptors == null || descriptors.isEmpty()) return; // Нечего вставлять
        var entities = new ArrayList<DescriptorEntity>(descriptors.size());
        for (var e : descriptors.entrySet()) {
            entities.add(mapper.toEntity(e.getKey(), e.getValue()));
        }
        jpaRepository.saveAll(entities);
        jpaRepository.flush();
    }
}
