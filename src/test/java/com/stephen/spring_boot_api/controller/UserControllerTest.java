package com.stephen.spring_boot_api.controller;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.stephen.spring_boot_api.dto.request.UserCreationRequest;
import com.stephen.spring_boot_api.entity.User;
import com.stephen.spring_boot_api.service.UserService;

@SpringBootTest
// annotation for mock request
@AutoConfigureMockMvc
// annotation for load test.properties. If not, will use application.properties
@TestPropertySource(locations = "/test.properties")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private UserCreationRequest userRequest;
    private User userResponse;
    private LocalDate dateOfBirth;

    @BeforeEach
    public void initData() {
        dateOfBirth = LocalDate.of(1990, 1, 1);
        UserCreationRequest userRequestMock = new UserCreationRequest();
        userRequestMock.setUsername("username");
        userRequestMock.setFirstName("firstName");
        userRequestMock.setLastName("lastName");
        userRequestMock.setPassword("password");
        userRequestMock.setDateOfBirth(dateOfBirth);
        userRequest = userRequestMock;

        User userResponseMock = new User();
        userResponseMock.setId("id");
        userResponseMock.setUsername("username");
        userResponseMock.setFirstName("firstName");
        userResponseMock.setLastName("lastName");
        userResponseMock.setPassword("password");
        userResponseMock.setDateOfBirth(dateOfBirth);
        userResponse = userResponseMock;
    }

    @Test
    void createUser_validRequest_success() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(userRequest);

        // mock request
        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(userResponse);

        // create a request
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(200));
    }
}
