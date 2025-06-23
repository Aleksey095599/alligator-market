package com.alligator.market.backend.quotes.stream.ccypair_feed_settings.service;

import com.alligator.market.backend.ccypairs.entity.Pair;
import com.alligator.market.backend.ccypairs.exceptions.PairNotFoundException;
import com.alligator.market.backend.ccypairs.repository.PairRepository;
import com.alligator.market.backend.quotes.stream.ccypair_feed_settings.dto.SettingsCreateDto;
import com.alligator.market.backend.quotes.stream.ccypair_feed_settings.dto.SettingsDto;
import com.alligator.market.backend.quotes.stream.ccypair_feed_settings.dto.SettingsUpdateDto;
import com.alligator.market.backend.quotes.stream.ccypair_feed_settings.entity.CcyPairFeedSettingsEntity;
import com.alligator.market.backend.quotes.stream.ccypair_feed_settings.exceptions.DuplicateSettingsException;
import com.alligator.market.backend.quotes.stream.ccypair_feed_settings.exceptions.SettingsNotFoundException;
import com.alligator.market.backend.quotes.stream.ccypair_feed_settings.mapper.CcyPairFeedSettingsMapper;
import com.alligator.market.backend.quotes.stream.ccypair_feed_settings.repository.SettingsRepository;
import com.alligator.market.backend.quotes.stream.providers.list.entity.Provider;
import com.alligator.market.backend.quotes.stream.providers.list.exceptions.ProviderNotFoundException;
import com.alligator.market.backend.quotes.stream.providers.list.repository.ProviderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация сервиса для операций с соответсвующее таблицей.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SettingsServiceImpl implements SettingsService {

    private final SettingsRepository repository;
    private final PairRepository pairRepository;
    private final ProviderRepository providerRepository;
    private final CcyPairFeedSettingsMapper mapper;

    //========================
    // Создать новые настройки
    //========================
    @Override
    public String createConfig(SettingsCreateDto dto) {

        repository.findByPair_PairAndProvider_Name(dto.pair(), dto.provider()).ifPresent(c -> {
            throw new DuplicateSettingsException(dto.pair(), dto.provider());
        });

        Pair pair = pairRepository.findByPair(dto.pair())
                .orElseThrow(() -> new PairNotFoundException(dto.pair()));

        // Для PUSH-интервал определяет провайдер, поэтому устанавливаем 0.
        int refreshMs = "PUSH".equals(dto.mode()) ? 0 : dto.refreshMs();

        Provider provider = providerRepository.findByName(dto.provider())
                .orElseThrow(() -> new ProviderNotFoundException(dto.provider()));

        CcyPairFeedSettingsEntity entity = mapper.toEntity(
                new com.alligator.market.domain.quotes.stream.settings.CcyPairFeedSettings(
                        dto.pair(),
                        dto.provider(),
                        dto.mode(),
                        dto.priority(),
                        refreshMs,
                        dto.enabled()
                ),
                pair,
                provider
        );

        CcyPairFeedSettingsEntity saved = repository.save(entity);
        log.info("CcyPairFeedSettingsEntity {}:{}:{} saved with id={}", dto.pair(), dto.provider(), dto.mode(), saved.getId());
        return "%s:%s:%s".formatted(saved.getPair().getPair(), saved.getProvider().getName(), saved.getMode());
    }

    //===================
    // Обновить настройки
    //===================
    @Override
    public void updateConfig(String pair, String provider, SettingsUpdateDto dto) {

        CcyPairFeedSettingsEntity entity = repository.findByPair_PairAndProvider_Name(pair, provider)
                .orElseThrow(() -> new SettingsNotFoundException(pair, provider));

        entity.setPriority(dto.priority());
        // Если режим PUSH, интервал задаётся провайдером, поэтому сохраняем 0.
        int refreshMs = "PUSH".equals(entity.getMode()) ? 0 : dto.refreshMs();
        entity.setRefreshMs(refreshMs);
        entity.setEnabled(dto.enabled());

        repository.save(entity);
        log.info("CcyPairFeedSettingsEntity {}:{} updated (id={})", pair, provider, entity.getId());
    }

    //==================
    // Удалить настройки
    //==================
    @Override
    public void deleteConfig(String pair, String provider) {

        CcyPairFeedSettingsEntity entity = repository.findByPair_PairAndProvider_Name(pair, provider)
                .orElseThrow(() -> new SettingsNotFoundException(pair, provider));

        repository.delete(entity);
        log.info("CcyPairFeedSettingsEntity {}:{} deleted (id={})", pair, provider, entity.getId());
    }

    //======================
    // Извлечь все настройки
    //======================
    @Override
    @Transactional(readOnly = true)
    public List<SettingsDto> findAll() {

        List<SettingsDto> result = repository.findAll(Sort.by("pair.pair", "provider.name", "mode"))
                .stream()
                .map(cfg -> new SettingsDto(
                        cfg.getPair().getPair(),
                        cfg.getProvider().getName(),
                        cfg.getMode(),
                        cfg.getPriority(),
                        cfg.getRefreshMs(),
                        cfg.isEnabled()
                ))
                .toList();
        log.debug("Found {} streaming configs", result.size());
        return result;
    }

}
