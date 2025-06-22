package com.alligator.market.backend.providers.service;

import com.alligator.market.backend.providers.dto.ProviderCreateDto;
import com.alligator.market.backend.providers.dto.ProviderDto;
import com.alligator.market.backend.providers.dto.ProviderUpdateDto;
import com.alligator.market.backend.providers.entity.Provider;
import com.alligator.market.backend.providers.exceptions.DuplicateProviderException;
import com.alligator.market.backend.providers.exceptions.ProviderNotFoundException;
import com.alligator.market.backend.providers.repository.ProviderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация сервиса для операций с провайдерами.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepository repository;

    //=====================
    // Создать нового провайдера
    //=====================
    @Override
    public String createProvider(ProviderCreateDto dto) {

        repository.findByName(dto.name()).ifPresent(p -> {
            throw new DuplicateProviderException(dto.name());
        });

        Provider entity = new Provider();
        entity.setName(dto.name());
        entity.setBaseUrl(dto.baseUrl());
        entity.setMode(dto.mode());
        entity.setApiKey(dto.apiKey());

        Provider saved = repository.save(entity);
        log.info("Provider {} saved with id={}", saved.getName(), saved.getId());
        return saved.getName();
    }

    //================
    // Обновить провайдера
    //================
    @Override
    public void updateProvider(String name, ProviderUpdateDto dto) {

        Provider provider = repository.findByName(name)
                .orElseThrow(() -> new ProviderNotFoundException(name));

        provider.setBaseUrl(dto.baseUrl());
        provider.setMode(dto.mode());
        provider.setApiKey(dto.apiKey());

        repository.save(provider);
        log.info("Provider {} updated (id={})", provider.getName(), provider.getId());
    }

    //=================
    // Удалить провайдера
    //=================
    @Override
    public void deleteProvider(String name) {

        Provider provider = repository.findByName(name)
                .orElseThrow(() -> new ProviderNotFoundException(name));

        repository.delete(provider);
        log.info("Provider {} deleted (id={})", provider.getName(), provider.getId());
    }

    //=====================
    // Извлечь всех провайдеров
    //=====================
    @Override
    @Transactional(readOnly = true)
    public List<ProviderDto> findAll() {

        List<ProviderDto> result = repository.findAll(Sort.by("name"))
                .stream()
                .map(p -> new ProviderDto(
                        p.getName(),
                        p.getBaseUrl(),
                        p.getMode(),
                        p.getApiKey()
                ))
                .toList();
        log.debug("Found {} providers", result.size());
        return result;
    }
}
