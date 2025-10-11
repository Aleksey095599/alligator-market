package com.alligator.market.backend.instrument.type.forex.spot.catalog.service;

import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotDuplicateException;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotNotFoundException;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.instrument.type.forex.spot.repository.FxSpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация сервиса {@link FxSpotUseCase}.
 * Содержит проверки и операции с инструментами FX_SPOT.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FxSpotUseCaseImpl implements FxSpotUseCase {

    private final FxSpotRepository fxSpotRepository;

    @Override
    public String create(FxSpot fxSpot) {
        // Проверяем, что инструмента с таким кодом нет, иначе выбрасываем доменное исключение
        fxSpotRepository.find(fxSpot.instrumentCode()).ifPresent(i -> {
            throw new FxSpotDuplicateException(fxSpot.instrumentCode());
        });
        // Сохраняем инструмент
        fxSpotRepository.save(fxSpot);
        log.info("FxSpot {} created", fxSpot.instrumentCode());
        return fxSpot.instrumentCode();
    }

    @Override
    public void update(FxSpot fxSpot) {
        // Из модели извлекаем код
        String instrumentCode = fxSpot.instrumentCode();
        // Ищем текущий инструмент
        FxSpot current = fxSpotRepository.find(instrumentCode)
                .orElseThrow(() -> new FxSpotNotFoundException(instrumentCode));
        // Обновляем текущий инструмент
        FxSpot updated = new FxSpot(
                current.base(),
                current.quote(),
                current.valueDate(),
                fxSpot.defaultQuoteFractionDigits() // Берем из переданной модели
        );
        // Сохраняем обновленный инструмент
        fxSpotRepository.save(updated);
        log.info("FxSpot {} updated", instrumentCode);
    }

    @Override
    public void delete(String code) {
        // Проверяем, что инструмент существует
        fxSpotRepository.find(code)
                .orElseThrow(() -> new FxSpotNotFoundException(code));
        fxSpotRepository.delete(code);
        log.info("FxSpot {} deleted", code);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FxSpot> findAll() {
        List<FxSpot> result = fxSpotRepository.findAll();
        log.debug("Found {} FX_SPOT instruments", result.size());
        return result;
    }
}
