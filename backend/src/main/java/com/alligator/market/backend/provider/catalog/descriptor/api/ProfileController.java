package com.alligator.market.backend.provider.catalog.descriptor.api;

import com.alligator.market.backend.common.web.ApiResponse;
import com.alligator.market.backend.common.web.ResponseEntityFactory;
import com.alligator.market.backend.provider.catalog.descriptor.api.dto.DescriptorDto;
import com.alligator.market.backend.provider.catalog.descriptor.service.DescriptorUseCase;
import com.alligator.market.backend.provider.catalog.descriptor.api.dto.DescriptorDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST-контроллер дескрипторов.
 */
@RestController
@RequestMapping("/api/v1/providers")
@RequiredArgsConstructor
public class ProfileController {

    private final DescriptorUseCase service;
    private final DescriptorDtoMapper mapper;

    /** Вернуть все активные профили. */
    @GetMapping
    public ResponseEntity<ApiResponse<List<DescriptorDto>>> getAll() {
        List<DescriptorDto> list = service.findAllActive().values().stream()
                .map(mapper::toDto)
                .toList();
        return ResponseEntityFactory.ok(list);
    }

    /** Вернуть все дескрипторы. */

}
