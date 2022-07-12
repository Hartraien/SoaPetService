package ru.hartraien.gatewayservice.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;
import ru.hartraien.gatewayservice.errorhandlers.CustomWebExceptionHandler;

import java.util.stream.Collectors;

@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(WebFluxAutoConfiguration.class)
public class ExceptionHandlingConfig {
    private final ServerProperties serverProperties;

    @Autowired
    public ExceptionHandlingConfig(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    @Bean
    @Order(-1)
    public ErrorWebExceptionHandler errorWebExceptionHandler(ErrorAttributes errorAttributes
            , WebProperties webProperties
            , ObjectProvider<ViewResolver> viewResolvers
            , ServerCodecConfigurer serverCodecConfigurer
            , ApplicationContext applicationContext) {

        CustomWebExceptionHandler exceptionHandler = new CustomWebExceptionHandler(errorAttributes, webProperties.getResources(), serverProperties.getError(), applicationContext);
        exceptionHandler.setViewResolvers(viewResolvers.orderedStream().collect(Collectors.toList()));
        exceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
        exceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
        return exceptionHandler;
    }
}
