package com.alligator.market.backend.provider.profile.service;

import com.alligator.market.backend.provider.profile.context_sync.ProviderContextScanner;
import com.alligator.market.domain.provider.profile.ProviderProfile;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

/* Интеграционный тест для сканера профилей провайдеров. */
@Disabled
@SpringBootTest
class ProviderContextScannerTest {

    @Autowired
    private ProviderContextScanner scanner;

    @Test
    void shouldPrintAllProfiles() {
        List<ProviderProfile> profiles = scanner.getProviderProfiles();
        profiles.forEach(System.out::println);
        assertFalse(profiles.isEmpty());
    }
}
