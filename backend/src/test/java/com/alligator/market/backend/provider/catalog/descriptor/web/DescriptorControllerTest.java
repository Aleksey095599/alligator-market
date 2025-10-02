package com.alligator.market.backend.provider.catalog.descriptor.web;

import com.alligator.market.backend.provider.catalog.descriptor.service.DescriptorUseCase;
import com.alligator.market.backend.provider.catalog.descriptor.web.dto.DescriptorDtoMapper;
import com.alligator.market.domain.provider.contract.descriptor.AccessMethod;
import com.alligator.market.domain.provider.contract.descriptor.DeliveryMode;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Тест-кейс контроллера дескрипторов провайдеров.
 */
@WebMvcTest(DescriptorController.class)
@Import(DescriptorDtoMapper.class)
class DescriptorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DescriptorUseCase useCase;

    /** Проверяем, что код провайдера присутствует в ответе. */
    @Test
    void getAll_ShouldExposeProviderCode() throws Exception {
        // Подготавливаем карту дескрипторов с сохранением порядка
        Map<String, ProviderDescriptor> descriptors = new LinkedHashMap<>();
        descriptors.put("twelve-free", new ProviderDescriptor(
                "Twelve Data",
                DeliveryMode.PULL,
                AccessMethod.API_POLL,
                true
        ));
        given(useCase.getAll()).willReturn(descriptors);

        mockMvc.perform(get("/api/v1/providers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].providerCode").value("twelve-free"));
    }
}
