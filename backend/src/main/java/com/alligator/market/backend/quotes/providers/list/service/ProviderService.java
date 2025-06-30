package com.alligator.market.backend.quotes.providers.list.service;

import com.alligator.market.backend.quotes.providers.list.dto.ProviderCreateDto;
import com.alligator.market.backend.quotes.providers.list.dto.ProviderDto;
import com.alligator.market.backend.quotes.providers.list.dto.ProviderUpdateDto;

import java.util.List;

/**
 * Интерфейс сервиса для операций с провайдерами.
 */
public interface ProviderService {

    String createProvider(ProviderCreateDto dto);

    void updateProvider(String name, ProviderUpdateDto dto);

    void deleteProvider(String name);

    List<ProviderDto> findAll();

}
