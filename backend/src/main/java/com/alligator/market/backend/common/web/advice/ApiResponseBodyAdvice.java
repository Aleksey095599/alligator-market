package com.alligator.market.backend.common.web.advice;

import com.alligator.market.backend.common.web.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.time.LocalDateTime;

/*
 * Оборачивает ответы контроллеров в ApiResponse для унификации формата ответов от API.
 * Перехватывает все ответы, не являющиеся ApiResponse, добавляет стандартные поля success,
 * message, timestamp и path для поддержания единого формата.
 */
@RestControllerAdvice
public class ApiResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final HttpServletRequest request;

    @Autowired
    public ApiResponseBodyAdvice(HttpServletRequest request) {
        this.request = request;
    }

    /* Решает, нужен ли перехват: true, если метод контроллера возвращает НЕ ApiResponse. */
    @Override
    public boolean supports(@NonNull MethodParameter returnType,
                            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        // Не оборачиваем, если уже ApiResponse → избегаем двойной упаковки
        return !ApiResponse.class.isAssignableFrom(returnType.getParameterType());
    }

    /* Оборачивает тело ответа в ApiResponse перед сериализацией. */
    @Override
    public Object beforeBodyWrite(@Nullable Object body,
                                  @NonNull MethodParameter returnType,
                                  @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest serverRequest,
                                  @NonNull ServerHttpResponse serverResponse) {
        return new ApiResponse<>(
                true,
                body,
                "OK",
                null,
                null,
                LocalDateTime.now(),
                request.getRequestURI()
        );
    }
}