package com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.web;

import com.alligator.market.backend.common.web.ApiResponse;
import com.alligator.market.backend.common.web.ResponseEntityFactory;
import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.web.dto.CurrencyDto;
import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.web.dto.UpdateCurrencyDto;
import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.web.dto.CurrencyDtoMapper;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.service.CurrencyUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
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
@Validated
public class CurrencyController {

    private final CurrencyUseCase service;
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
        List<CurrencyDto> currencyDtoList = service.getAll()
                .stream()
                .map(mapper::toDto)
                .toList();
        return ResponseEntityFactory.ok(currencyDtoList);
    }
}
