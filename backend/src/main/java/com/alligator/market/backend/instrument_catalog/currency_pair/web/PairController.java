package com.alligator.market.backend.instrument_catalog.currency_pair.web;

import com.alligator.market.backend.common.web.ApiResponse;
import com.alligator.market.backend.common.web.ResponseEntityFactory;
import com.alligator.market.backend.instrument_catalog.currency_pair.web.dto.PairCreateDto;
import com.alligator.market.backend.instrument_catalog.currency_pair.web.dto.PairDto;
import com.alligator.market.backend.instrument_catalog.currency_pair.web.dto.PairUpdateDto;
import com.alligator.market.domain.instrument.currency_pair.model.CurrencyPair;
import com.alligator.market.backend.instrument_catalog.currency_pair.service.CurrencyPairService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * REST-контроллер валютных пар.
 */
@RestController
@RequestMapping("/api/v1/pairs")
@RequiredArgsConstructor
@Slf4j
public class PairController {

    private final CurrencyPairService service;

    //========================================
    //               Операции
    //========================================

    /** Создать валютную пару. */
    @PostMapping
    public ResponseEntity<ApiResponse<String>> create(@RequestBody @Valid PairCreateDto dto) {

        // Формируем модель валютной пары из DTO
        CurrencyPair currencyPair = new CurrencyPair(
                dto.base(),
                dto.quote(),
                dto.decimal()
        );

        // Сохраняем пару и получаем её код
        String pairCode = service.create(currencyPair);

        // Формируем ссылку на созданный ресурс
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{pairCode}")
                .buildAndExpand(pairCode)
                .toUri();

        return ResponseEntityFactory.created(location, pairCode);
    }

    /** Обновить валютную пару. */
    @PutMapping("/{base}/{quote}")
    public ResponseEntity<ApiResponse<Void>> update(
            @PathVariable String base,
            @PathVariable String quote,
            @RequestBody @Valid PairUpdateDto dto) {

        // Формируем модель валютной пары для обновления
        CurrencyPair currencyPair = new CurrencyPair(
                base,
                quote,
                dto.decimal()
        );

        service.update(currencyPair);

        return ResponseEntityFactory.ok(null);
    }

    /** Удалить валютную пару. */
    @DeleteMapping("/{base}/{quote}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable String base,
            @PathVariable String quote) {

        service.delete(base, quote);

        return ResponseEntityFactory.ok(null);
    }

    /** Вернуть все валютные пары. */
    @GetMapping
    public ResponseEntity<ApiResponse<List<PairDto>>> getAll() {

        // Извлекаем сервисом список валютных-пар-моделей и на лету преобразуем его в список валютных-пар-DTO
        List<PairDto> pairDtoList = service.findAll()
                .stream()
                .map(p -> new PairDto(
                        p.base(),
                        p.quote(),
                        p.pairCode(),
                        p.decimal()
                ))
                .toList();

        return ResponseEntityFactory.ok(pairDtoList);
    }
}
