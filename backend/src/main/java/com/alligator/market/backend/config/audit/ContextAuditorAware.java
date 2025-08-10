package com.alligator.market.backend.config.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Реализация AuditorAware.
 */
@RequiredArgsConstructor
public class ContextAuditorAware implements AuditorAware<String> {

    private final String serviceName;

    /**
     * Вернуть текущего аудитора из SecurityContext в виде "serviceName-by-user".
     */
    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication != null ? authentication.getName() : "internal-service";
        return Optional.of(serviceName + "-by-" + user);
    }
}


