package com.alligator.market.backend.quotes.stream.settings.service;

import com.alligator.market.backend.pairs.entity.Pair;
import com.alligator.market.backend.pairs.exceptions.PairNotFoundException;
import com.alligator.market.backend.pairs.repository.PairRepository;
import com.alligator.market.backend.quotes.stream.settings.dto.SettingsCreateDto;
import com.alligator.market.backend.quotes.stream.settings.dto.SettingsDto;
import com.alligator.market.backend.quotes.stream.settings.dto.SettingsUpdateDto;
import com.alligator.market.backend.quotes.stream.settings.entity.CcyPairFeedSettingsEntity;
import com.alligator.market.backend.quotes.stream.settings.exceptions.DuplicateSettingsException;
import com.alligator.market.backend.quotes.stream.settings.exceptions.SettingsNotFoundException;
import com.alligator.market.backend.quotes.stream.settings.mapper.CcyPairFeedSettingsMapper;
import com.alligator.market.backend.quotes.stream.settings.repository.SettingsRepository;
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
    private final CcyPairFeedSettingsMapper mapper;

    //========================
    // Создать новые настройки
    //========================
    @Override
    public String createConfig(SettingsCreateDto dto) {

        repository.findByPair_PairAndProvider(dto.pair(), dto.provider()).ifPresent(c -> {
            throw new DuplicateSettingsException(dto.pair(), dto.provider());
        });

        Pair pair = pairRepository.findByPair(dto.pair())
                .orElseThrow(() -> new PairNotFoundException(dto.pair()));

        CcyPairFeedSettingsEntity entity = mapper.toEntity(
                new com.alligator.market.domain.model.CcyPairFeedSettings(
                        dto.pair(),
                        dto.provider(),
                        dto.priority(),
                        dto.refreshMs(),
                        dto.enabled()
                ),
                pair
        );

        CcyPairFeedSettingsEntity saved = repository.save(entity);
        log.info("CcyPairFeedSettingsEntity {}:{} saved with id={}", dto.pair(), dto.provider(), saved.getId());
        return "%s:%s".formatted(saved.getPair().getPair(), saved.getProvider());
    }

    //===================
    // Обновить настройки
    //===================
    @Override
    public void updateConfig(String pair, String provider, SettingsUpdateDto dto) {

        CcyPairFeedSettingsEntity entity = repository.findByPair_PairAndProvider(pair, provider)
                .orElseThrow(() -> new SettingsNotFoundException(pair, provider));

        entity.setPriority(dto.priority());
        entity.setRefreshMs(dto.refreshMs());
        entity.setEnabled(dto.enabled());

        repository.save(entity);
        log.info("CcyPairFeedSettingsEntity {}:{} updated (id={})", pair, provider, entity.getId());
    }

    //==================
    // Удалить настройки
    //==================
    @Override
    public void deleteConfig(String pair, String provider) {

        CcyPairFeedSettingsEntity entity = repository.findByPair_PairAndProvider(pair, provider)
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

        List<SettingsDto> result = repository.findAll(Sort.by("pair.pair", "provider"))
                .stream()
                .map(cfg -> new SettingsDto(
                        cfg.getPair().getPair(),
                        cfg.getProvider(),
                        cfg.getPriority(),
                        cfg.getRefreshMs(),
                        cfg.isEnabled()
                ))
                .toList();
        log.debug("Found {} streaming configs", result.size());
        return result;
    }

}
