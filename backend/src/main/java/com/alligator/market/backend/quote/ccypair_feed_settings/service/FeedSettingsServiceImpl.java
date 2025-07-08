package com.alligator.market.backend.quote.ccypair_feed_settings.service;

import com.alligator.market.backend.instrument.forex.currency_pair.entity.PairEntity;
import com.alligator.market.backend.instrument.forex.currency_pair.exception.PairNotFoundException;
import com.alligator.market.backend.instrument.forex.currency_pair.repository.PairRepository;
import com.alligator.market.backend.quote.ccypair_feed_settings.dto.FeedSettingsCreateDto;
import com.alligator.market.backend.quote.ccypair_feed_settings.dto.FeedSettingsDto;
import com.alligator.market.backend.quote.ccypair_feed_settings.dto.FeedSettingsUpdateDto;
import com.alligator.market.backend.quote.ccypair_feed_settings.entity.CcyPairFeedSettingsEntity;
import com.alligator.market.backend.quote.ccypair_feed_settings.exceptions.DuplicateSettingsException;
import com.alligator.market.backend.quote.ccypair_feed_settings.exceptions.SettingsNotFoundException;
import com.alligator.market.backend.quote.ccypair_feed_settings.mapper.CcyPairFeedSettingsMapper;
import com.alligator.market.backend.quote.ccypair_feed_settings.repository.CcyPairFeedSettingsRepository;
import com.alligator.market.backend.quote.providers.list.entity.Provider;
import com.alligator.market.backend.quote.providers.list.exceptions.ProviderNotFoundException;
import com.alligator.market.backend.quote.providers.list.repository.ProviderRepository;
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
public class FeedSettingsServiceImpl implements FeedSettingsService {

    private final CcyPairFeedSettingsRepository repository;
    private final PairRepository pairRepository;
    private final ProviderRepository providerRepository;
    private final CcyPairFeedSettingsMapper mapper;

    //========================
    // Создать новые настройки
    //========================
    @Override
    public String createSettings(FeedSettingsCreateDto dto) {

        repository.findByPair_PairAndProvider_Name(dto.pair(), dto.provider()).ifPresent(c -> {
            throw new DuplicateSettingsException(dto.pair(), dto.provider());
        });

        PairEntity pair = pairRepository.findByPair(dto.pair())
                .orElseThrow(() -> new PairNotFoundException(dto.pair()));

        Provider provider = providerRepository.findByName(dto.provider())
                .orElseThrow(() -> new ProviderNotFoundException(dto.provider()));

        // Для PUSH-интервал определяет провайдер, поэтому устанавливаем 0.
        int fetchPeriodMs = "PUSH".equals(provider.getMode()) ? 0 : dto.fetchPeriodMs();

        CcyPairFeedSettingsEntity entity = mapper.toEntity(
                new com.alligator.market.domain.quote.CcyPairFeedSettings(
                        dto.pair(),
                        dto.provider(),
                        dto.priority(),
                        fetchPeriodMs,
                        dto.enabled()
                ),
                pair,
                provider
        );

        CcyPairFeedSettingsEntity saved = repository.save(entity);
        log.info("CcyPairFeedSettingsEntity {}:{} saved with id={}", dto.pair(), dto.provider(), saved.getId());
        return "%s:%s".formatted(saved.getPair().getPair(), saved.getProvider().getName());
    }

    //===================
    // Обновить настройки
    //===================
    @Override
    public void updateSettings(String pair, String provider, FeedSettingsUpdateDto dto) {

        CcyPairFeedSettingsEntity entity = repository.findByPair_PairAndProvider_Name(pair, provider)
                .orElseThrow(() -> new SettingsNotFoundException(pair, provider));

        entity.setPriority(dto.priority());
        // Если режим PUSH, интервал задаётся провайдером, поэтому сохраняем 0.
        int fetchPeriodMs = "PUSH".equals(entity.getProvider().getMode()) ? 0 : dto.fetchPeriodMs();
        entity.setFetchPeriodMs(fetchPeriodMs);
        entity.setEnabled(dto.enabled());

        repository.save(entity);
        log.info("CcyPairFeedSettingsEntity {}:{} updated (id={})", pair, provider, entity.getId());
    }

    //==================
    // Удалить настройки
    //==================
    @Override
    public void deleteSettings(String pair, String provider) {

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
    public List<FeedSettingsDto> findAll() {

        List<FeedSettingsDto> result = repository.findAll(Sort.by("pair.pair", "provider.name"))
                .stream()
                .map(settings -> new FeedSettingsDto(
                        settings.getPair().getPair(),
                        settings.getProvider().getName(),
                        settings.getPriority(),
                        settings.getFetchPeriodMs(),
                        settings.isEnabled()
                ))
                .toList();
        log.debug("Found {} streaming settings", result.size());
        return result;
    }

}
