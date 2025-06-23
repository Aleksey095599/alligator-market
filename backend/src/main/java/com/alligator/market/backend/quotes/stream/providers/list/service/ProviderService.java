package com.alligator.market.backend.quotes.stream.providers.list.service;

import com.alligator.market.backend.quotes.stream.providers.list.dto.ProviderCreateDto;
import com.alligator.market.backend.quotes.stream.providers.list.dto.ProviderDto;
import com.alligator.market.backend.quotes.stream.providers.list.dto.ProviderUpdateDto;

import java.util.List;

/**
 * Интерфейс сервиса для операций с провайдерами.
 */
public interface ProviderService {

    String createProvider(ProviderCreateDto dto);

    void updateProvider(String name, String mode, ProviderUpdateDto dto);

    void deleteProvider(String name, String mode);

    List<ProviderDto> findAll();

}
