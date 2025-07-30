package com.alligator.market.backend.instrument.type.forex.currency_pair.web;

import com.alligator.market.backend.common.web.ApiResponse;
import com.alligator.market.backend.common.web.ResponseEntityFactory;
import com.alligator.market.backend.instrument.type.forex.currency_pair.dto.PairCreateDto;
import com.alligator.market.backend.instrument.type.forex.currency_pair.dto.PairDto;
import com.alligator.market.backend.instrument.type.forex.currency_pair.dto.PairUpdateDto;
import com.alligator.market.domain.instrument.type.forex.currency_pair.CurrencyPair;
import com.alligator.market.backend.instrument.type.forex.currency_pair.service.CurrencyPairService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * REST-контроллер для операций с валютными парами.
 */
@RestController
@RequestMapping("/api/v1/pairs")
@RequiredArgsConstructor
@Slf4j
public class PairController {

    private final CurrencyPairService service;

    //===================
    // Создать новую пару
    //===================
    @PostMapping
    public ResponseEntity<ApiResponse<String>> create(@RequestBody @Valid PairCreateDto dto) {

        // Формируем модель валютной пары из DTO
        CurrencyPair currencyPair = new CurrencyPair(
                dto.code1(),
                dto.code2(),
                dto.code1() + dto.code2(),
                dto.decimal()
        );

        // Применяем к валютной паре метод сервиса, который вернет код пары из созданной новой записи
        String pairCode = service.createPair(currencyPair);

        // Формируем ссылку на созданный ресурс
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{pairCode}")
                .buildAndExpand(pairCode)
                .toUri();

        return ResponseEntityFactory.created(location, pairCode);
    }

    //==============
    // Обновить пару
    //==============
    @PutMapping("/{base}/{quote}")
    public ResponseEntity<ApiResponse<Void>> update(
            @PathVariable String base,
            @PathVariable String quote,
            @RequestBody @Valid PairUpdateDto dto) {

        // Формируем модель валютной пары для обновления
        CurrencyPair currencyPair = new CurrencyPair(
                base,
                quote,
                base + quote,
                dto.decimal()
        );

        service.updatePair(currencyPair);

        return ResponseEntityFactory.ok(null);
    }

    //=============
    // Удалить пару
    //=============
    @DeleteMapping("/{base}/{quote}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable String base,
            @PathVariable String quote) {

        service.deletePair(base + quote);

        return ResponseEntityFactory.ok(null);
    }

    //=================
    // Вернуть все пары
    //=================
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
