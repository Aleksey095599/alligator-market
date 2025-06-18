package com.alligator.market.backend.quotes.config.service;

import com.alligator.market.backend.quotes.config.dto.FxPairStreamingConfigCreateDto;
import com.alligator.market.backend.quotes.config.dto.FxPairStreamingConfigDto;
import com.alligator.market.backend.quotes.config.dto.FxPairStreamingConfigUpdateDto;

import java.util.List;

/**
 * Интерфейс сервиса конфигурации стрима котировок для заданной валютной пары.
 */
public interface FxPairStreamingConfigService {

    String createConfig(FxPairStreamingConfigCreateDto dto);

    void updateConfig(String pair, String provider, FxPairStreamingConfigUpdateDto dto);

    void deleteConfig(String pair, String provider);

    List<FxPairStreamingConfigDto> findAll();

}
