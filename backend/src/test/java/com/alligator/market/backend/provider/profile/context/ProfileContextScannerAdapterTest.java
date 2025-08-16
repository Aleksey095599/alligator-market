package com.alligator.market.backend.provider.profile.context;

import com.alligator.market.backend.provider.profile.context.scanner.ProfileContextScannerAdapter;
import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Интеграционный тест для сканера профилей провайдеров.
 * Выводит в консоль список провайдеров.
 */
@Disabled
@SpringBootTest
class ProfileContextScannerAdapterTest {

    @Autowired
    private ProfileContextScannerAdapter scanner;

    @Test
    void shouldPrintAllProfiles() {
        List<ProviderProfile> profiles = scanner.getProviderProfiles();
        profiles.forEach(System.out::println);
        assertFalse(profiles.isEmpty());
    }
}
