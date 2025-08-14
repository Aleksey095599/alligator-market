package com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.web;

import com.alligator.market.backend.common.web.ApiResponse;
import com.alligator.market.backend.common.web.ResponseEntityFactory;
import com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.web.dto.CurrencyDto;
import com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.web.dto.CurrencyUpdateDto;
import com.alligator.market.domain.instrument.type.forex.outright.reference.currency.model.Currency;
import com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.service.CurrencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * REST-контроллер валют.
 */
@RestController
@RequestMapping("/api/v1/currencies")
@RequiredArgsConstructor
@Slf4j
public class CurrencyController {

    private final CurrencyService service;

    //========================================
    //               Операции
    //========================================

    /** Создать валюту. */
    @PostMapping
    public ResponseEntity<ApiResponse<String>> create(@RequestBody @Valid CurrencyDto dto) {

        // Создаем модель валюты на основе полученного DTO
        Currency currency = new Currency(
                dto.code(),
                dto.name(),
                dto.country(),
                dto.decimal()
        );

        // Применяем к валюте метод сервиса, который вернет код валюты из созданной новой записи
        String code = service.createCurrency(currency);

        // Формируем ссылку на созданный ресурс
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{code}")
                .buildAndExpand(code)
                .toUri();

        return ResponseEntityFactory.created(location, code);
    }

    /** Обновить валюту. */
    @PutMapping("/{code}")
    public ResponseEntity<ApiResponse<Void>> update(
            @PathVariable String code,
            @RequestBody @Valid CurrencyUpdateDto dto) {

        // Создаем модель валюты на основе полученного DTO
        Currency currency = new Currency(
                code,
                dto.name(),
                dto.country(),
                dto.decimal()
        );

        service.updateCurrency(currency);

        return ResponseEntityFactory.ok(null);
    }

    /** Удалить валюту. */
    @DeleteMapping("/{code}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String code) {

        service.deleteCurrency(code);

        return ResponseEntityFactory.ok(null);
    }

    /** Вернуть все валюты. */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CurrencyDto>>> getAll() {

        // Извлекаем сервисом список валют-моделей и на лету преобразуем его в список валют-DTO
        List<CurrencyDto> currencyDtoList = service.findAll()
                .stream()
                .map(c -> new CurrencyDto(
                        c.code(),
                        c.name(),
                        c.country(),
                        c.decimal()
                ))
                .toList();

        return ResponseEntityFactory.ok(currencyDtoList);
    }
}
