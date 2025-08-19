package com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.api;

import com.alligator.market.backend.common.web.ApiResponse;
import com.alligator.market.backend.common.web.ResponseEntityFactory;
import com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.api.dto.CurrencyDto;
import com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.api.dto.UpdateCurrencyDto;
import com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.api.dto.CurrencyDtoMapper;
import com.alligator.market.domain.instrument.type.forex.outright.reference.currency.model.Currency;
import com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.service.crud.CurrencyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * REST-контроллер для валют.
 */
@RestController
@RequestMapping("/api/v1/currencies")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CurrencyController {

    private final CurrencyService service;
    private final CurrencyDtoMapper mapper;

    /** Создать валюту. */
    @PostMapping
    public ResponseEntity<ApiResponse<String>> create(@RequestBody @Valid CurrencyDto dto) {
        Currency currency = mapper.toDomain(dto);
        String code = service.createCurrency(currency);
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
            @PathVariable @Pattern(regexp = "^[A-Z]{3}$") String code,
            @RequestBody @Valid UpdateCurrencyDto dto) {
        Currency currency = mapper.toDomain(code, dto);
        service.updateCurrency(currency);
        return ResponseEntityFactory.ok(null);
    }

    /** Удалить валюту. */
    @DeleteMapping("/{code}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable @Pattern(regexp = "^[A-Z]{3}$") String code) {
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
