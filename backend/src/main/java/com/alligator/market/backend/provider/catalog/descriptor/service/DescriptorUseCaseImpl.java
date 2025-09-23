package com.alligator.market.backend.provider.catalog.descriptor.service;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.reppository.ProviderDescriptorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация сервиса {@link DescriptorUseCase}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DescriptorUseCaseImpl implements DescriptorUseCase {

    private final ProviderDescriptorRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<ProviderDescriptor> getAll() {
        List<ProviderDescriptor> descriptors = repository.findAll();
        log.debug("Found {} provider descriptors", descriptors.size());
        return descriptors;
    }
}

