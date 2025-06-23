package com.alligator.market.backend.quotes.stream.providers.list.service;

import com.alligator.market.backend.quotes.stream.ccypair_feed_settings.repository.SettingsRepository;
import com.alligator.market.backend.quotes.stream.providers.list.dto.ProviderCreateDto;
import com.alligator.market.backend.quotes.stream.providers.list.dto.ProviderDto;
import com.alligator.market.backend.quotes.stream.providers.list.dto.ProviderUpdateDto;
import com.alligator.market.backend.quotes.stream.providers.list.entity.Provider;
import com.alligator.market.backend.quotes.stream.providers.list.exceptions.DuplicateProviderException;
import com.alligator.market.backend.quotes.stream.providers.list.exceptions.ProviderNotFoundException;
import com.alligator.market.backend.quotes.stream.providers.list.exceptions.ProviderUsedInSettingsException;
import com.alligator.market.backend.quotes.stream.providers.list.repository.ProviderRepository;
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
    private final SettingsRepository settingsRepository;

    //==========================
    // Создать нового провайдера
    //==========================
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
        log.info("Provider {}:{} saved with id={}", saved.getName(), saved.getMode(), saved.getId());
        return saved.getName();
    }

    //====================
    // Обновить провайдера
    //====================
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

    //===================
    // Удалить провайдера
    //===================
    @Override
    public void deleteProvider(String name) {

        Provider provider = repository.findByName(name)
                .orElseThrow(() -> new ProviderNotFoundException(name));

        // Проверка, что провайдер не используется в настройках
        if (settingsRepository.existsByProvider_Name(name)) {
            throw new ProviderUsedInSettingsException(name);
        }

        repository.delete(provider);
        log.info("Provider {} deleted (id={})", provider.getName(), provider.getId());
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
