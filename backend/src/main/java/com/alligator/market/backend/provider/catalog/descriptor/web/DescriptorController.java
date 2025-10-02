package com.alligator.market.backend.provider.catalog.descriptor.web;

import com.alligator.market.backend.common.web.ApiResponse;
import com.alligator.market.backend.common.web.ResponseEntityFactory;
import com.alligator.market.backend.provider.catalog.descriptor.web.dto.DescriptorDto;
import com.alligator.market.backend.provider.catalog.descriptor.web.dto.DescriptorDtoMapper;
import com.alligator.market.backend.provider.catalog.descriptor.service.DescriptorUseCase;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * REST-контроллер дескрипторов.
 */
@RestController
@RequestMapping("/api/v1/providers")
@RequiredArgsConstructor
public class DescriptorController {

    private final DescriptorUseCase service;
    private final DescriptorDtoMapper mapper;

    /** Вернуть все дескрипторы. */
    @GetMapping
    public ResponseEntity<ApiResponse<List<DescriptorDto>>> getAll() {
        // Сервис возвращает доменные дескрипторы, индексированные по коду провайдера
        Map<String, ProviderDescriptor> descriptors = service.getAll();
        List<DescriptorDto> response = descriptors.entrySet().stream()
                .map(entry -> mapper.toDto(entry.getKey(), entry.getValue()))
                .toList();
        return ResponseEntityFactory.ok(response);
    }
}
