package ru.hartraien.userservice.webTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hartraien.userservice.DTOs.NameValidationResponse;
import ru.hartraien.userservice.DTOs.UserServiceResponse;
import ru.hartraien.userservice.DTOs.UsernameAndPasswordDTO;
import ru.hartraien.userservice.DTOs.UsernameDTO;

import java.nio.charset.StandardCharsets;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class webtest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_Valid() {
        String username = "username";
        String password = "password";
        UsernameAndPasswordDTO usernameAndPasswordDTO = new UsernameAndPasswordDTO();
        usernameAndPasswordDTO.setUsername(username);
        usernameAndPasswordDTO.setPassword(password);
        try {
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usernameAndPasswordDTO))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();
            String contentAsString = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
            UserServiceResponse userServiceResponse = objectMapper.readValue(contentAsString, UserServiceResponse.class);
            Assertions.assertEquals(username, userServiceResponse.getUsername());
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
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usernameAndPasswordDTO))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());
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
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usernameAndPasswordDTO))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usernameAndPasswordDTO))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();
            String contentAsString = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
            UserServiceResponse userServiceResponse = objectMapper.readValue(contentAsString, UserServiceResponse.class);
            Assertions.assertEquals(username, userServiceResponse.getUsername());
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
        try {
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
    void getUserInfo_Valid() {
        String username = "username";
        String password = "password";
        UsernameAndPasswordDTO usernameAndPasswordDTO = new UsernameAndPasswordDTO();
        usernameAndPasswordDTO.setUsername(username);
        usernameAndPasswordDTO.setPassword(password);
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usernameAndPasswordDTO))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/getUserInfo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usernameAndPasswordDTO))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();
            String contentAsString = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
            UserServiceResponse userServiceResponse = objectMapper.readValue(contentAsString, UserServiceResponse.class);
            Assertions.assertEquals(username, userServiceResponse.getUsername());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void getUserInfo_Invalid() {
        String username = "username";
        String password = "password";
        UsernameAndPasswordDTO usernameAndPasswordDTO = new UsernameAndPasswordDTO();
        usernameAndPasswordDTO.setUsername(username);
        usernameAndPasswordDTO.setPassword(password);
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/getUserInfo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usernameAndPasswordDTO))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void validName_valid() {
        String username = "username";
        UsernameDTO usernameDTO = new UsernameDTO();
        usernameDTO.setUsername(username);

        NameValidationResponse nameValidationResponse = NameValidationResponse.validName();

        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/validName")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usernameDTO))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(nameValidationResponse)));
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void validName_Invalid() {
        String username = "username";
        String password = "password";
        UsernameAndPasswordDTO usernameAndPasswordDTO = new UsernameAndPasswordDTO();
        usernameAndPasswordDTO.setUsername(username);
        usernameAndPasswordDTO.setPassword(password);
        UsernameDTO usernameDTO = new UsernameDTO();
        usernameDTO.setUsername(username);

        NameValidationResponse nameValidationResponse = NameValidationResponse.invalidName();

        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usernameAndPasswordDTO))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());
            mockMvc.perform(MockMvcRequestBuilders.post("/validName")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usernameDTO))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(nameValidationResponse)));
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }
}
