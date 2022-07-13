package ru.hartraien.authservice.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.hartraien.authservice.Service.UserServiceConnector;

@Configuration
public class TestConfig {
    @Bean
    @Primary
    public UserServiceConnector userServiceConnector() {
        return Mockito.mock(UserServiceConnector.class);
    }
}
