package ru.hartraien.petservice.webtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hartraien.petservice.DTOs.*;
import ru.hartraien.petservice.Service.PetTypeService;
import ru.hartraien.petservice.entities.PetType;
import ru.hartraien.petservice.entities.Sex;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.stream.Collectors;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FullTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PetTypeService petTypeService;

    @Test
    void petTypesTest() throws Exception {
        Long id = 1L;
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/pet-types")
                        .header("id", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        PetTypesList petTypesList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8), PetTypesList.class);

        Assertions.assertEquals(petTypeService.getAllTypes().stream().map(PetType::getTypeName).collect(Collectors.toList()), petTypesList.getTypes());
    }

    @Test
    void addPet_Valid() throws Exception {
        Long id = 1L;

        PetInsertInput petInsertInput = new PetInsertInput();
        petInsertInput.setName("Pet");
        petInsertInput.setType("cat");
        petInsertInput.setSex(Sex.MALE);
        petInsertInput.setBirthdate(LocalDate.now());

        mockMvc.perform(MockMvcRequestBuilders.post("/pets")
                        .header("id", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petInsertInput))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void addPet_Invalid() throws Exception {
        Long id = 1L;

        PetInsertInput petInsertInput = new PetInsertInput();
        petInsertInput.setName("Pet");
        petInsertInput.setType("prop");
        petInsertInput.setSex(Sex.MALE);
        petInsertInput.setBirthdate(LocalDate.now());

        mockMvc.perform(MockMvcRequestBuilders.post("/pets")
                        .header("id", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petInsertInput))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getAllPets_Valid() throws Exception {
        Long id = 1L;

        PetInsertInput petInsertInput = new PetInsertInput();
        petInsertInput.setName("Pet");
        petInsertInput.setType("cat");
        petInsertInput.setSex(Sex.MALE);
        petInsertInput.setBirthdate(LocalDate.now());

        mockMvc.perform(MockMvcRequestBuilders.post("/pets")
                        .header("id", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petInsertInput))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/pets")
                        .header("id", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        PetInfoList petInfoList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8), PetInfoList.class);


        Assertions.assertTrue(petInfoList
                .getPets().stream()
                .map(PetInfo::getName)
                .collect(Collectors.toList())
                .contains(petInsertInput.getName()));
    }

    @Test
    void getFullPetInfo_Valid() throws Exception {
        Long id = 1L;

        PetInsertInput petInsertInput = new PetInsertInput();
        petInsertInput.setName("Pet");
        petInsertInput.setType("cat");
        petInsertInput.setSex(Sex.MALE);
        petInsertInput.setBirthdate(LocalDate.now());

        mockMvc.perform(MockMvcRequestBuilders.post("/pets")
                        .header("id", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petInsertInput))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/pets/" + 1)
                        .header("id", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

        PetFullInfo petFullInfo = objectMapper.readValue(contentAsString, PetFullInfo.class);

        Assertions.assertEquals(petInsertInput.getName(), petFullInfo.getName());
        Assertions.assertEquals(petInsertInput.getType(), petFullInfo.getType());
        Assertions.assertEquals(petInsertInput.getBirthdate(), petFullInfo.getBirthdate());
        Assertions.assertEquals(petInsertInput.getSex(), petFullInfo.getSex());
    }

    @Test
    void getFullPetInfo_Invalid() throws Exception {
        Long id = 1L;

        PetInsertInput petInsertInput = new PetInsertInput();
        petInsertInput.setName("Pet");
        petInsertInput.setType("cat");
        petInsertInput.setSex(Sex.MALE);
        petInsertInput.setBirthdate(LocalDate.now());

        mockMvc.perform(MockMvcRequestBuilders.post("/pets")
                        .header("id", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petInsertInput))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/pets/" + 2)
                        .header("id", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

    }

    @Test
    void updatePet_Valid() throws Exception {
        Long id = 1L;

        PetInsertInput petInsertInput = new PetInsertInput();
        petInsertInput.setName("Pet");
        petInsertInput.setType("cat");
        petInsertInput.setSex(Sex.MALE);
        petInsertInput.setBirthdate(LocalDate.now());

        mockMvc.perform(MockMvcRequestBuilders.post("/pets")
                        .header("id", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petInsertInput))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        PetUpdateInput petUpdateInput = new PetUpdateInput();
        petUpdateInput.setId(1L);
        petUpdateInput.setName("newName");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/pets")
                        .header("id", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petUpdateInput))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

        PetFullInfo petFullInfo = objectMapper.readValue(contentAsString, PetFullInfo.class);

        Assertions.assertEquals(petUpdateInput.getName(), petFullInfo.getName());
        Assertions.assertEquals(petInsertInput.getType(), petFullInfo.getType());
        Assertions.assertEquals(petInsertInput.getBirthdate(), petFullInfo.getBirthdate());
        Assertions.assertEquals(petInsertInput.getSex(), petFullInfo.getSex());
    }

    @Test
    void updatePet_Invalid() throws Exception {
        Long id = 1L;

        PetInsertInput petInsertInput = new PetInsertInput();
        petInsertInput.setName("Pet");
        petInsertInput.setType("cat");
        petInsertInput.setSex(Sex.MALE);
        petInsertInput.setBirthdate(LocalDate.now());

        mockMvc.perform(MockMvcRequestBuilders.post("/pets")
                        .header("id", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petInsertInput))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        PetUpdateInput petUpdateInput = new PetUpdateInput();
        petUpdateInput.setId(2L);
        petUpdateInput.setName("newName");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/pets")
                        .header("id", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petUpdateInput))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

    }

    @Test
    void deletePet_Valid() throws Exception {
        Long id = 1L;

        PetInsertInput petInsertInput = new PetInsertInput();
        petInsertInput.setName("Pet");
        petInsertInput.setType("cat");
        petInsertInput.setSex(Sex.MALE);
        petInsertInput.setBirthdate(LocalDate.now());

        mockMvc.perform(MockMvcRequestBuilders.post("/pets")
                        .header("id", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petInsertInput))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/pets/" + 1)
                        .header("id", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    void deletePet_Invalid() throws Exception {
        Long id = 1L;

        PetInsertInput petInsertInput = new PetInsertInput();
        petInsertInput.setName("Pet");
        petInsertInput.setType("cat");
        petInsertInput.setSex(Sex.MALE);
        petInsertInput.setBirthdate(LocalDate.now());

        mockMvc.perform(MockMvcRequestBuilders.post("/pets")
                        .header("id", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petInsertInput))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/pets/" + 2)
                        .header("id", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

    }

    @Test
    void deletePet_Unowned() throws Exception {
        Long id = 1L;

        PetInsertInput petInsertInput = new PetInsertInput();
        petInsertInput.setName("Pet");
        petInsertInput.setType("cat");
        petInsertInput.setSex(Sex.MALE);
        petInsertInput.setBirthdate(LocalDate.now());

        mockMvc.perform(MockMvcRequestBuilders.post("/pets")
                        .header("id", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petInsertInput))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/pets/" + 1)
                        .header("id", id+1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

    }
}
