package com.alligator.market.backend.quotes.config.service;

import com.alligator.market.backend.pairs.entity.Pair;
import com.alligator.market.backend.pairs.exceptions.PairNotFoundException;
import com.alligator.market.backend.pairs.repository.PairRepository;
import com.alligator.market.backend.quotes.config.dto.FxPairStreamingConfigCreateDto;
import com.alligator.market.backend.quotes.config.dto.FxPairStreamingConfigDto;
import com.alligator.market.backend.quotes.config.dto.FxPairStreamingConfigUpdateDto;
import com.alligator.market.backend.quotes.config.entity.FxPairStreamingConfig;
import com.alligator.market.backend.quotes.config.exceptions.DuplicateFxPairStreamingConfigException;
import com.alligator.market.backend.quotes.config.exceptions.FxPairStreamingConfigNotFoundException;
import com.alligator.market.backend.quotes.config.mapper.FxPairStreamingConfigMapper;
import com.alligator.market.backend.quotes.config.repository.FxPairStreamingConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация сервиса конфигурации стрима котировок для заданной валютной пары.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FxPairStreamingConfigServiceImpl implements FxPairStreamingConfigService {

    private final FxPairStreamingConfigRepository repository;
    private final PairRepository pairRepository;
    private final FxPairStreamingConfigMapper mapper;

    //====================================
    // Создать новую конфигурацию для пары
    //====================================
    @Override
    public String createConfig(FxPairStreamingConfigCreateDto dto) {

        repository.findByPair_PairAndProvider(dto.pair(), dto.provider()).ifPresent(c -> {
            throw new DuplicateFxPairStreamingConfigException(dto.pair(), dto.provider());
        });

        Pair pair = pairRepository.findByPair(dto.pair())
                .orElseThrow(() -> new PairNotFoundException(dto.pair()));

        FxPairStreamingConfig entity = mapper.toEntity(
                new com.alligator.market.domain.model.CcyPairFeedSettings(
                        dto.pair(),
                        dto.provider(),
                        dto.priority(),
                        dto.refreshMs(),
                        dto.enabled()
                ),
                pair
        );

        FxPairStreamingConfig saved = repository.save(entity);
        log.info("FxPairStreamingConfig {}:{} saved with id={}", dto.pair(), dto.provider(), saved.getId());
        return "%s:%s".formatted(saved.getPair().getPair(), saved.getProvider());
    }

    //===========================
    // Обновить конфигурацию пары
    //===========================
    @Override
    public void updateConfig(String pair, String provider, FxPairStreamingConfigUpdateDto dto) {

        FxPairStreamingConfig entity = repository.findByPair_PairAndProvider(pair, provider)
                .orElseThrow(() -> new FxPairStreamingConfigNotFoundException(pair, provider));

        entity.setPriority(dto.priority());
        entity.setRefreshMs(dto.refreshMs());
        entity.setEnabled(dto.enabled());

        repository.save(entity);
        log.info("FxPairStreamingConfig {}:{} updated (id={})", pair, provider, entity.getId());
    }

    //==========================
    // Удалить конфигурацию пары
    //==========================
    @Override
    public void deleteConfig(String pair, String provider) {

        FxPairStreamingConfig entity = repository.findByPair_PairAndProvider(pair, provider)
                .orElseThrow(() -> new FxPairStreamingConfigNotFoundException(pair, provider));

        repository.delete(entity);
        log.info("FxPairStreamingConfig {}:{} deleted (id={})", pair, provider, entity.getId());
    }

    //=============================
    // Извлечь все конфигурации пар
    //=============================
    @Override
    @Transactional(readOnly = true)
    public List<FxPairStreamingConfigDto> findAll() {

        List<FxPairStreamingConfigDto> result = repository.findAll(Sort.by("pair.pair", "provider"))
                .stream()
                .map(cfg -> new FxPairStreamingConfigDto(
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
