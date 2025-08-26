package com.alligator.market.backend.instrument.type.forex.spot.catalog.service;

import com.alligator.market.domain.instrument.type.forex.spot.service.FxSpotCatalog;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация сервиса для работы с инструментами типа FX_SPOT.
 * Делегирует выполнение операций доменному классу {@link FxSpotCatalog},
 * который содержит бизнес-логику и проверки.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FxSpotServiceImpl implements FxSpotService {

    private final FxSpotCatalog domainCatalog;

    @Override
    public String create(FxSpot instrument) {
        String code = domainCatalog.create(instrument);
        log.info("FxSpot {} created", code);
        return code;
    }

    @Override
    public void updateQuoteDecimal(String code, int quoteDecimal) {
        domainCatalog.updateQuoteDecimal(code, quoteDecimal);
        log.info("FxSpot {} updated", code);
    }

    @Override
    public void delete(String code) {
        domainCatalog.delete(code);
        log.info("FxSpot {} deleted", code);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FxSpot> findAll() {
        List<FxSpot> result = domainCatalog.getAll();
        log.debug("Found {} FX_SPOT instruments", result.size());
        return result;
    }
}
