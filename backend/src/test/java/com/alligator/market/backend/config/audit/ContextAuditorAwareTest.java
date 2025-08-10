package com.alligator.market.backend.config.audit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тесты для {@link ContextAuditorAware}.
 */
class ContextAuditorAwareTest {

    private final ContextAuditorAware auditorAware = new ContextAuditorAware("test-service");

    @AfterEach
    void cleanContext() {
        SecurityContextHolder.clearContext();
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void returnsUserFromSecurityContext() {
        // Устанавливаем пользователя в контекст безопасности
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("elon", "N/A")
        );

        Optional<String> auditor = auditorAware.getCurrentAuditor();
        assertEquals("elon@test-service", auditor.orElseThrow());
    }

    @Test
    void returnsStubUserForHttpRequest() {
        // Создаем HTTP-запрос без авторизации
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Optional<String> auditor = auditorAware.getCurrentAuditor();
        assertEquals("admin_dev@test-service", auditor.orElseThrow());
    }

    @Test
    void returnsInternalServiceWithoutRequest() {
        Optional<String> auditor = auditorAware.getCurrentAuditor();
        assertEquals("internal-service@test-service", auditor.orElseThrow());
    }
}
