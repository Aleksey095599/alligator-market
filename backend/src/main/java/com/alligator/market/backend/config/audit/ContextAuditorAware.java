package com.alligator.market.backend.config.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * Реализация AuditorAware, использующая ServiceAuditorContext.
 */
@RequiredArgsConstructor
public class ContextAuditorAware implements AuditorAware<String> {

    private final ServiceAuditorContext context;

    /**
     * Возвращает текущего аудитора из контекста.
     */
    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        return Optional.of(context.get().orElse("dev_admin"));
    }
}

