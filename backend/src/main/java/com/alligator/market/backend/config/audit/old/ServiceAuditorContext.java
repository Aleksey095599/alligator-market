package com.alligator.market.backend.config.audit.old;

import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Контекст аудита, хранящий идентификатор сервиса в ThreadLocal.
 */
@Component
public class ServiceAuditorContext {

    private final ThreadLocal<String> serviceId = new ThreadLocal<>();

    /** Установить идентификатор сервиса для текущего потока. */
    public void set(String serviceId) {
        this.serviceId.set(serviceId);
    }

    /** Очистить идентификатор сервиса в текущем потоке. */
    public void clear() {
        serviceId.remove();
    }

    /** Получить идентификатор сервиса из текущего потока. */
    public Optional<String> get() {
        return Optional.ofNullable(serviceId.get());
    }

    /** Выполнить действие с указанным идентификатором сервиса. */
    public void runWith(String serviceId, Runnable action) {
        set(serviceId);
        try {
            action.run();
        } finally {
            clear();
        }
    }
}

