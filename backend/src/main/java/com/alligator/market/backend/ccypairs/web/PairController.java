package com.alligator.market.backend.ccypairs.web;

import com.alligator.market.backend.ccypairs.dto.PairCreateDto;
import com.alligator.market.backend.ccypairs.dto.PairDto;
import com.alligator.market.backend.ccypairs.dto.PairUpdateDto;
import com.alligator.market.backend.ccypairs.service.PairService;
import com.alligator.market.backend.common.web.dto.ApiResponse;
import com.alligator.market.backend.common.web.util.ResponseEntityFactory;
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

    private final PairService service;

    //===================
    // Создать новую пару
    //===================
    @PostMapping
    public ResponseEntity<ApiResponse<String>> create(@RequestBody @Valid PairCreateDto dto) {

        String pair = service.createPair(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{pair}")
                .buildAndExpand(pair)
                .toUri();
        return ResponseEntityFactory.created(location, pair);
    }

    //==============
    // Обновить пару
    //==============
    @PutMapping("/{pair}")
    public ResponseEntity<ApiResponse<Void>> update(
            @PathVariable String pair,
            @RequestBody @Valid PairUpdateDto dto) {
        service.updatePair(pair, dto);
        return ResponseEntityFactory.ok(null);
    }

    //=============
    // Удалить пару
    //=============
    @DeleteMapping("/{pair}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String pair) {

        service.deletePair(pair);
        return ResponseEntityFactory.ok(null);
    }

    //=================
    // Вернуть все пары
    //=================
    @GetMapping
    public ResponseEntity<ApiResponse<List<PairDto>>> getAll() {

        return ResponseEntityFactory.ok(service.findAll());
    }

}
