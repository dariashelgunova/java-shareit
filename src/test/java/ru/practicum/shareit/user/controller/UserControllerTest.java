package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;
    private User user;
    private UserRequestDto userRequestDto;
    private UserRequestDto userRequestDtoWithId;
    private MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
        mapper.registerModule(new JavaTimeModule());

        user = createUser(1L);
        userRequestDto = new UserRequestDto(null, user.getEmail(), user.getName());
        userRequestDtoWithId = new UserRequestDto(user.getId(), user.getEmail(), user.getName());
    }

    @Test
    public void givenUserDto_whenCreate_thenStatus200andUserReturned() throws Exception {
        when(userService.create(any())).thenReturn(user);
        String response = mvc.perform(post("/users")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(userRequestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(mapper.writeValueAsString(userRequestDtoWithId), response);
    }

    @Test
    public void givenUserDto_whenFindAll_thenStatus200andUserReturned() throws Exception {
        when(userService.findAll()).thenReturn(Collections.singletonList(user));
        String response = mvc.perform(get("/users")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(mapper.writeValueAsString(Collections.singletonList(userRequestDtoWithId)), response);
    }

    @Test
    public void givenUserDto_whenUpdate_thenStatus200andUserReturned() throws Exception {
        String updatedName = "updatedName";
        UserRequestDto updatedUser = new UserRequestDto();
        updatedUser.setName(updatedName);
        user.setName(updatedName);
        userRequestDtoWithId.setName(updatedName);

        when(userService.update(any(), any())).thenReturn(user);
        String response = mvc.perform(patch("/users/{userId}", user.getId())
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(updatedUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(mapper.writeValueAsString(userRequestDtoWithId), response);
    }

    @Test
    public void givenUserDto_whenFindById_thenStatus200andUserReturned() throws Exception {
        when(userService.findById(any())).thenReturn(user);
        String response = mvc.perform(get("/users/{userId}", user.getId())
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(mapper.writeValueAsString(userRequestDtoWithId), response);
    }

    @Test
    public void givenUserDto_whenDeleteById_thenStatus200() throws Exception {
        String response = mvc.perform(delete("/users/{userId}", user.getId())
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertTrue(response.isBlank());
    }

    @Test
    public void givenUserDto_whenDeleteAll_thenStatus200() throws Exception {
        String response = mvc.perform(delete("/users")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertTrue(response.isBlank());
    }


    private User createUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setName("name" + id);
        user.setEmail("yandex" + id + "@yandex.ru");
        return user;
    }
}