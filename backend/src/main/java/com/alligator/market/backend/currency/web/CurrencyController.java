package com.alligator.market.backend.currency.web;

import com.alligator.market.backend.common.web.dto.ApiResponse;
import com.alligator.market.backend.common.web.util.ResponseEntityFactory;
import com.alligator.market.backend.currency.dto.CreateCurrencyRequest;
import com.alligator.market.backend.currency.service.CurrencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/* REST-контроллер для операций с валютами. */
@RestController
@RequestMapping("/api/v1/currencies")
@RequiredArgsConstructor
@Slf4j
public class CurrencyController {

    private final CurrencyService service;

    //============================
    // POST — создать новую валюту
    //============================
    @PostMapping
    public ResponseEntity<ApiResponse<String>> create(@RequestBody @Valid CreateCurrencyRequest dto) {

        // Сервис возвращает код валюты
        String code = service.createCurrency(dto);

        // Формируем ссылку на созданный ресурс
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{code}")
                .buildAndExpand(code)
                .toUri();

        return ResponseEntityFactory.created(location, code);
    }
}