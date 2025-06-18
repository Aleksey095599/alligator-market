package com.alligator.market.backend.quotes.config.web;

import com.alligator.market.backend.common.web.dto.ApiResponse;
import com.alligator.market.backend.common.web.util.ResponseEntityFactory;
import com.alligator.market.backend.quotes.config.dto.FxPairStreamingConfigCreateDto;
import com.alligator.market.backend.quotes.config.dto.FxPairStreamingConfigDto;
import com.alligator.market.backend.quotes.config.dto.FxPairStreamingConfigUpdateDto;
import com.alligator.market.backend.quotes.config.service.FxPairStreamingConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * REST-контроллер для управления конфигурациями стрима котировок.
 */
@RestController
@RequestMapping("/api/v1/streaming-configs")
@RequiredArgsConstructor
@Slf4j
public class FxPairStreamingConfigController {

    private final FxPairStreamingConfigService service;

    //======================================================
    // Создать новую конфигурацию для заданной валютной пары
    //======================================================
    @PostMapping
    public ResponseEntity<ApiResponse<String>> create(@RequestBody @Valid FxPairStreamingConfigCreateDto dto) {

        String id = service.createConfig(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{pair}/{provider}")
                .buildAndExpand(dto.pair(), dto.provider())
                .toUri();
        return ResponseEntityFactory.created(location, id);
    }

    //===========================================
    // Обновить конфигурацию по паре и провайдеру
    //===========================================
    @PutMapping("/{pair}/{provider}")
    public ResponseEntity<ApiResponse<Void>> update(
            @PathVariable String pair,
            @PathVariable String provider,
            @RequestBody @Valid FxPairStreamingConfigUpdateDto dto) {
        service.updateConfig(pair, provider, dto);
        return ResponseEntityFactory.ok(null);
    }

    //==========================================
    // Удалить конфигурацию по паре и провайдеру
    //==========================================
    @DeleteMapping("/{pair}/{provider}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable String pair,
            @PathVariable String provider) {
        service.deleteConfig(pair, provider);
        return ResponseEntityFactory.ok(null);
    }

    //=========================
    // Вернуть все конфигурации
    //=========================
    @GetMapping
    public ResponseEntity<ApiResponse<List<FxPairStreamingConfigDto>>> getAll() {

        return ResponseEntityFactory.ok(service.findAll());
    }
}
