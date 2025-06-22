package com.alligator.market.backend.quotes.stream.prodiders.list.web;

import com.alligator.market.backend.common.web.dto.ApiResponse;
import com.alligator.market.backend.common.web.util.ResponseEntityFactory;
import com.alligator.market.backend.quotes.stream.prodiders.list.dto.ProviderCreateDto;
import com.alligator.market.backend.quotes.stream.prodiders.list.dto.ProviderDto;
import com.alligator.market.backend.quotes.stream.prodiders.list.dto.ProviderUpdateDto;
import com.alligator.market.backend.quotes.stream.prodiders.list.service.ProviderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * REST-контроллер для операций с провайдерами.
 */
@RestController
@RequestMapping("/api/v1/providers")
@RequiredArgsConstructor
@Slf4j
public class ProviderController {

    private final ProviderService service;

    //=====================
    // Создать нового провайдера
    //=====================
    @PostMapping
    public ResponseEntity<ApiResponse<String>> create(@RequestBody @Valid ProviderCreateDto dto) {

        String name = service.createProvider(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{name}")
                .buildAndExpand(name)
                .toUri();
        return ResponseEntityFactory.created(location, name);
    }

    //================
    // Обновить провайдера
    //================
    @PutMapping("/{name}")
    public ResponseEntity<ApiResponse<Void>> update(
            @PathVariable String name,
            @RequestBody @Valid ProviderUpdateDto dto) {
        service.updateProvider(name, dto);
        return ResponseEntityFactory.ok(null);
    }

    //=================
    // Удалить провайдера
    //=================
    @DeleteMapping("/{name}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String name) {

        service.deleteProvider(name);
        return ResponseEntityFactory.ok(null);
    }

    //=====================
    // Вернуть всех провайдеров
    //=====================
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProviderDto>>> getAll() {

        return ResponseEntityFactory.ok(service.findAll());
    }
}
