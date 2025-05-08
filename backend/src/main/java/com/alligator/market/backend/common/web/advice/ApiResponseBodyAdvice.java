package com.alligator.market.backend.common.web.advice;

import com.alligator.market.backend.common.web.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.time.LocalDateTime;

/*
 * Перехватывает успешные ответы контроллера и оборачивает их в унифицированный формат ответа:
 * com.alligator.market.backend.common.web.dto.ApiResponse
 */
@RestControllerAdvice
public class ApiResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final HttpServletRequest request;

    @Autowired
    public ApiResponseBodyAdvice(HttpServletRequest request) {
        this.request = request;
    }

    /* Задает исключения, когда не нужно оборачивать */
    @Override
    public boolean supports(@NonNull MethodParameter returnType,
                            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        Class<?> declaredType = returnType.getParameterType();
        boolean alreadyWrapped = ApiResponse.class.isAssignableFrom(declaredType);
        boolean isResponseEntity = ResponseEntity.class.isAssignableFrom(declaredType);
        // Не оборачиваем, если уже обернутое или если это ResponseEntity
        return !(alreadyWrapped || isResponseEntity);
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