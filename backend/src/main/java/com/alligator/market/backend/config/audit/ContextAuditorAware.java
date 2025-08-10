package com.alligator.market.backend.config.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Optional;

/**
 * Реализация AuditorAware.
 */
@RequiredArgsConstructor
public class ContextAuditorAware implements AuditorAware<String> {

    /** Имя текущего сервиса. */
    private final String serviceName;

    /** Временная заглушка для имени пользователя ("user"). */
    private static final String STUB_USER = "admin_dev";

    /**
     * Имя для внутренних сервисов.
     * Используется как условный "user", когда действие выполняется внутренним сервисом, а не пользователем.
     */
    private static final String INTERNAL_SERVICE = "internal-service";

    /**
     * Вернуть аудитора в формате "user@service".
     */
    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String user;

        if (authentication != null) {
            // Имя пришло из SecurityContext
            user = authentication.getName();
        } else if (RequestContextHolder.getRequestAttributes() != null) {
            // HTTP-запрос без авторизации → используем заглушку
            user = STUB_USER;
        } else {
            // Внутренний сервис
            user = INTERNAL_SERVICE;
        }

        return Optional.of(user + "@" + serviceName);
    }
}


