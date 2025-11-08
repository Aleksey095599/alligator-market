package com.alligator.market.backend.config.audit.filter;

import com.alligator.market.backend.config.audit.context.AuditContext;
import com.alligator.market.backend.config.audit.context.AuditContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Фильтр для установки контекста аудита на время HTTP-запроса.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // <-- Выполнить как можно раньше
public class DevAuditContextFilter extends OncePerRequestFilter {

    // Временные актор и via для этапа разработки
    private static final String DEV_ACTOR = "dev_admin";
    private static final String DEV_VIA = "dev_http";

    @Override
    protected void doFilterInternal(
            @org.springframework.lang.NonNull HttpServletRequest request,
            @org.springframework.lang.NonNull HttpServletResponse response,
            @org.springframework.lang.NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        var ctx = new AuditContext(DEV_ACTOR, DEV_VIA);
        try {
            // Устанавливаем контекст на время обработки запроса и гарантированно восстанавливаем его после.
            AuditContextHolder.runWith(ctx, () -> {
                try {
                    filterChain.doFilter(request, response);
                } catch (IOException | ServletException e) {
                    // Пробрасываем checked-исключения далее по цепочке
                    throw new RuntimeException(e);
                }
            });
        } catch (RuntimeException ex) {
            // Восстанавливаем checked-типы, чтобы не менять поведение контейнера
            var cause = ex.getCause();
            if (cause instanceof IOException io) throw io;
            if (cause instanceof ServletException se) throw se;
            throw ex;
        }
    }
}
