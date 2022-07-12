package ru.hartraien.gatewayservice.errorhandlers;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public class CustomWebExceptionHandler extends DefaultErrorWebExceptionHandler {
    public CustomWebExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources, ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resources, errorProperties, applicationContext);
    }

    @Override
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        int status;
        Map<String, Object> responseBodyMap = new HashMap<>();
        Throwable error = getError(request);

        Map<String, Object> errorAttributes = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));

        status = getHttpStatus(errorAttributes);

        responseBodyMap.put("error code", status);
        HttpStatus httpStatus = HttpStatus.valueOf(status);
        if (HttpStatus.SERVICE_UNAVAILABLE.equals(httpStatus))
            responseBodyMap.put("message", "Service is unavailable");
        else if (httpStatus.is5xxServerError())
            responseBodyMap.put("message", "Such service does not exists");
        else if (httpStatus.is4xxClientError())
            responseBodyMap.put("message", "No such service available");

        responseBodyMap.put("exception message", error.getMessage());

        return ServerResponse
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(responseBodyMap));
    }
}
