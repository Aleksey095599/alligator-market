package com.alligator.market.backend.quotes.config.service;

import com.alligator.market.domain.model.FxPairStreamingConfig;

import java.util.List;

/**
 * Интерфейс сервиса настроек стрима котировок для заданной валютной пары.
 */
public interface FxPairStreamingConfigService {

    void saveConfig(FxPairStreamingConfig cfg);

    List<FxPairStreamingConfig> findAll();

}
