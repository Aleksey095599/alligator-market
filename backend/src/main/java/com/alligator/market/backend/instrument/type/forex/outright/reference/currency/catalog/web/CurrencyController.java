package com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.web;

import com.alligator.market.backend.common.web.ApiResponse;
import com.alligator.market.backend.common.web.ResponseEntityFactory;
import com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.web.dto.CurrencyDto;
import com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.web.dto.UpdateCurrencyDto;
import com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.web.mapper.CurrencyDtoMapper;
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
    private final CurrencyDtoMapper mapper;

    /** Создать валюту. */
    @PostMapping
    public ResponseEntity<ApiResponse<String>> create(@RequestBody @Valid CurrencyDto dto) {

        Currency currency = mapper.toDomain(dto);

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
            @RequestBody @Valid UpdateCurrencyDto dto) {

        Currency currency = mapper.toDomain(code, dto);

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

        List<CurrencyDto> currencyDtoList = service.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();

        return ResponseEntityFactory.ok(currencyDtoList);
    }
}
