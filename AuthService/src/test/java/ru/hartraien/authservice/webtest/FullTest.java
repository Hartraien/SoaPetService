package ru.hartraien.authservice.webtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hartraien.authservice.DTOs.TokenRequest;
import ru.hartraien.authservice.DTOs.TokenResponse;
import ru.hartraien.authservice.DTOs.UserServiceResponse;
import ru.hartraien.authservice.DTOs.UsernameAndPasswordDTO;
import ru.hartraien.authservice.Exceptions.UserServiceFailedInputException;
import ru.hartraien.authservice.Service.UserServiceConnector;
import ru.hartraien.authservice.config.TestConfig;

import java.nio.charset.StandardCharsets;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Import(TestConfig.class)
public class FullTest {

    @Autowired
    private UserServiceConnector userServiceConnector;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        Mockito.reset(userServiceConnector);
    }

    @Test
    void register_Valid() {
        String username = "username";
        String password = "password";
        UsernameAndPasswordDTO usernameAndPasswordDTO = new UsernameAndPasswordDTO();
        usernameAndPasswordDTO.setUsername(username);
        usernameAndPasswordDTO.setPassword(password);

        UserServiceResponse userServiceResponse = new UserServiceResponse();
        userServiceResponse.setId(1);
        userServiceResponse.setUsername(username);


        try {
            Mockito.when(userServiceConnector.register(Mockito.any())).thenReturn(userServiceResponse);
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usernameAndPasswordDTO))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();
            String contentAsString = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
            TokenResponse tokenResponse = objectMapper.readValue(contentAsString, TokenResponse.class);
            Assertions.assertTrue(tokenResponse.getAccessToken() != null && !tokenResponse.getAccessToken().isBlank());
            Assertions.assertTrue(tokenResponse.getRefreshToken() != null && !tokenResponse.getRefreshToken().isBlank());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void register_Invalid() {
        String username = "username";
        String password = "password";
        UsernameAndPasswordDTO usernameAndPasswordDTO = new UsernameAndPasswordDTO();
        usernameAndPasswordDTO.setUsername(username);
        usernameAndPasswordDTO.setPassword(password);
        UserServiceResponse userServiceResponse = new UserServiceResponse();
        userServiceResponse.setId(1);
        userServiceResponse.setUsername(username);

        String errorMessage = "error message";
        try {
            Mockito.when(userServiceConnector.register(Mockito.any())).thenReturn(userServiceResponse);
            mockMvc.perform(MockMvcRequestBuilders.post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usernameAndPasswordDTO))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());
            Mockito.doThrow(new UserServiceFailedInputException(errorMessage)).when(userServiceConnector).register(Mockito.any());
            mockMvc.perform(MockMvcRequestBuilders.post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usernameAndPasswordDTO))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }


    @Test
    void login_Valid() {
        String username = "username";
        String password = "password";
        UsernameAndPasswordDTO usernameAndPasswordDTO = new UsernameAndPasswordDTO();
        usernameAndPasswordDTO.setUsername(username);
        usernameAndPasswordDTO.setPassword(password);

        UserServiceResponse userServiceResponse = new UserServiceResponse();
        userServiceResponse.setId(1);
        userServiceResponse.setUsername(username);


        try {
            Mockito.when(userServiceConnector.register(Mockito.any())).thenReturn(userServiceResponse);
            mockMvc.perform(MockMvcRequestBuilders.post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usernameAndPasswordDTO))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();
            Mockito.when(userServiceConnector.loginUser(Mockito.any())).thenReturn(userServiceResponse);
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usernameAndPasswordDTO))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            String contentAsString = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
            TokenResponse tokenResponse = objectMapper.readValue(contentAsString, TokenResponse.class);
            Assertions.assertTrue(tokenResponse.getAccessToken() != null && !tokenResponse.getAccessToken().isBlank());
            Assertions.assertTrue(tokenResponse.getRefreshToken() != null && !tokenResponse.getRefreshToken().isBlank());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void login_Invalid() {
        String username = "username";
        String password = "password";
        UsernameAndPasswordDTO usernameAndPasswordDTO = new UsernameAndPasswordDTO();
        usernameAndPasswordDTO.setUsername(username);
        usernameAndPasswordDTO.setPassword(password);

        String errorMessage = "error message";
        try {
            Mockito.doThrow(new UserServiceFailedInputException(errorMessage)).when(userServiceConnector).loginUser(Mockito.any());
            mockMvc.perform(MockMvcRequestBuilders.post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usernameAndPasswordDTO))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void refreshToken_Valid() {
        String username = "username";
        String password = "password";
        UsernameAndPasswordDTO usernameAndPasswordDTO = new UsernameAndPasswordDTO();
        usernameAndPasswordDTO.setUsername(username);
        usernameAndPasswordDTO.setPassword(password);

        UserServiceResponse userServiceResponse = new UserServiceResponse();
        userServiceResponse.setId(1);
        userServiceResponse.setUsername(username);


        try {
            Mockito.when(userServiceConnector.register(Mockito.any())).thenReturn(userServiceResponse);
            MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usernameAndPasswordDTO))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            TokenResponse tokenResponse1 = objectMapper.readValue(mvcResult1.getResponse().getContentAsString(StandardCharsets.UTF_8), TokenResponse.class);

            TokenRequest tokenRequest = new TokenRequest();
            tokenRequest.setToken(tokenResponse1.getRefreshToken());

            Mockito.when(userServiceConnector.checkUserByUsernameAndId(Mockito.anyLong(), Mockito.any())).thenReturn(userServiceResponse);
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/refreshToken")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tokenRequest))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            String contentAsString = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
            TokenResponse tokenResponse = objectMapper.readValue(contentAsString, TokenResponse.class);
            Assertions.assertTrue(tokenResponse.getAccessToken() != null && !tokenResponse.getAccessToken().isBlank());
            Assertions.assertTrue(tokenResponse.getRefreshToken() != null && !tokenResponse.getRefreshToken().isBlank());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void refreshToken_Invalid() {
        String username = "username";
        String password = "password";
        UsernameAndPasswordDTO usernameAndPasswordDTO = new UsernameAndPasswordDTO();
        usernameAndPasswordDTO.setUsername(username);
        usernameAndPasswordDTO.setPassword(password);

        UserServiceResponse userServiceResponse = new UserServiceResponse();
        userServiceResponse.setId(1);
        userServiceResponse.setUsername(username);

        String errorMessage = "error message";

        try {
            Mockito.when(userServiceConnector.register(Mockito.any())).thenReturn(userServiceResponse);
            MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usernameAndPasswordDTO))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            TokenResponse tokenResponse1 = objectMapper.readValue(mvcResult1.getResponse().getContentAsString(StandardCharsets.UTF_8), TokenResponse.class);

            TokenRequest tokenRequest = new TokenRequest();
            tokenRequest.setToken(tokenResponse1.getRefreshToken());

            Mockito.doThrow(new UserServiceFailedInputException(errorMessage)).when(userServiceConnector).checkUserByUsernameAndId(Mockito.anyLong(), Mockito.any());

            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/refreshToken")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tokenRequest))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andReturn();
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void verifyToken_Valid() {
        String username = "username";
        String password = "password";
        UsernameAndPasswordDTO usernameAndPasswordDTO = new UsernameAndPasswordDTO();
        usernameAndPasswordDTO.setUsername(username);
        usernameAndPasswordDTO.setPassword(password);

        UserServiceResponse userServiceResponse = new UserServiceResponse();
        userServiceResponse.setId(1);
        userServiceResponse.setUsername(username);


        try {
            Mockito.when(userServiceConnector.register(Mockito.any())).thenReturn(userServiceResponse);
            MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usernameAndPasswordDTO))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            TokenResponse tokenResponse1 = objectMapper.readValue(mvcResult1.getResponse().getContentAsString(StandardCharsets.UTF_8), TokenResponse.class);

            TokenRequest tokenRequest = new TokenRequest();
            tokenRequest.setToken(tokenResponse1.getRefreshToken());

            Mockito.when(userServiceConnector.checkUserByUsernameAndId(Mockito.anyLong(), Mockito.any())).thenReturn(userServiceResponse);

            mockMvc.perform(MockMvcRequestBuilders.post("/verifyToken")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tokenRequest))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void verifyToken_Invalid() {
        String username = "username";
        String password = "password";
        UsernameAndPasswordDTO usernameAndPasswordDTO = new UsernameAndPasswordDTO();
        usernameAndPasswordDTO.setUsername(username);
        usernameAndPasswordDTO.setPassword(password);

        UserServiceResponse userServiceResponse = new UserServiceResponse();
        userServiceResponse.setId(1);
        userServiceResponse.setUsername(username);

        String errorMessage = "error message";

        try {
            Mockito.when(userServiceConnector.register(Mockito.any())).thenReturn(userServiceResponse);
            MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders.post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usernameAndPasswordDTO))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            TokenResponse tokenResponse1 = objectMapper.readValue(mvcResult1.getResponse().getContentAsString(StandardCharsets.UTF_8), TokenResponse.class);

            TokenRequest tokenRequest = new TokenRequest();
            tokenRequest.setToken(tokenResponse1.getRefreshToken());

            Mockito.doThrow(new UserServiceFailedInputException(errorMessage)).when(userServiceConnector).checkUserByUsernameAndId(Mockito.anyLong(), Mockito.any());
            mockMvc.perform(MockMvcRequestBuilders.post("/verifyToken")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tokenRequest))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }
}
