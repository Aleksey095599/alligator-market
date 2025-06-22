package com.alligator.market.backend.quotes.stream.prodiders.list.service;

import com.alligator.market.backend.quotes.stream.prodiders.list.dto.ProviderCreateDto;
import com.alligator.market.backend.quotes.stream.prodiders.list.dto.ProviderDto;
import com.alligator.market.backend.quotes.stream.prodiders.list.dto.ProviderUpdateDto;
import com.alligator.market.backend.quotes.stream.prodiders.list.entity.Provider;
import com.alligator.market.backend.quotes.stream.prodiders.list.exceptions.DuplicateProviderException;
import com.alligator.market.backend.quotes.stream.prodiders.list.exceptions.ProviderNotFoundException;
import com.alligator.market.backend.quotes.stream.prodiders.list.repository.ProviderRepository;
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

    //==========================
    // Создать нового провайдера
    //==========================
    @Override
    public String createProvider(ProviderCreateDto dto) {

        repository.findByNameAndMode(dto.name(), dto.mode()).ifPresent(p -> {
            throw new DuplicateProviderException(dto.name(), dto.mode());
        });

        Provider entity = new Provider();
        entity.setName(dto.name());
        entity.setBaseUrl(dto.baseUrl());
        entity.setMode(dto.mode());
        entity.setApiKey(dto.apiKey());

        Provider saved = repository.save(entity);
        log.info("Provider {}:{} saved with id={}", saved.getName(), saved.getMode(), saved.getId());
        return saved.getName();
    }

    //====================
    // Обновить провайдера
    //====================
    @Override
    public void updateProvider(String name, String mode, ProviderUpdateDto dto) {

        Provider provider = repository.findByNameAndMode(name, mode)
                .orElseThrow(() -> new ProviderNotFoundException(name, mode));

        if (!mode.equals(dto.mode())) {
            repository.findByNameAndMode(name, dto.mode()).ifPresent(p -> {
                throw new DuplicateProviderException(name, dto.mode());
            });
        }

        provider.setBaseUrl(dto.baseUrl());
        provider.setMode(dto.mode());
        provider.setApiKey(dto.apiKey());

        repository.save(provider);
        log.info("Provider {}:{} updated (id={})", provider.getName(), provider.getMode(), provider.getId());
    }

    //===================
    // Удалить провайдера
    //===================
    @Override
    public void deleteProvider(String name, String mode) {

        Provider provider = repository.findByNameAndMode(name, mode)
                .orElseThrow(() -> new ProviderNotFoundException(name, mode));

        repository.delete(provider);
        log.info("Provider {}:{} deleted (id={})", provider.getName(), provider.getMode(), provider.getId());
    }

    //=========================
    // Извлечь всех провайдеров
    //=========================
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
