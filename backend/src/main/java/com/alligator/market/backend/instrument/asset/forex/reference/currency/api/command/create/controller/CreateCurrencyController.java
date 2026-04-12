package com.alligator.market.backend.instrument.asset.forex.reference.currency.api.command.create.controller;

import com.alligator.market.backend.common.web.response.ApiResponse;
import com.alligator.market.backend.common.web.response.ResponseEntityFactory;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.dto.common.CurrencyDto;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.dto.mapper.CurrencyDtoMapper;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.command.create.CreateCurrencyService;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.Currency;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * REST-контроллер для create use case валюты.
 */
@RestController
@RequestMapping("/api/v1/currencies")
@RequiredArgsConstructor
public class CreateCurrencyController {

    private final CreateCurrencyService createCurrencyService;

    /**
     * Создать валюту.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<String>> create(@RequestBody CurrencyDto dto) {

        Currency created = createCurrencyService.create(CurrencyDtoMapper.toDomain(dto));
        // Формируем location для созданного ресурса по его коду.
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{code}")
                .buildAndExpand(created.code().value())
                .toUri();
        return ResponseEntityFactory.created(location, created.code().value());
    }
}
