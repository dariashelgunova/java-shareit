package ru.practicum.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.exception.NotFoundObjectException;
import ru.practicum.user.dto.UserRequestDto;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @MockBean
    private UserService userService;
    private User user;
    private UserRequestDto userRequestDto;
    private UserRequestDto userRequestDtoWithId;
    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setup() {
        mapper.registerModule(new JavaTimeModule());

        user = createUser(1L);
        userRequestDto = new UserRequestDto(null, user.getEmail(), user.getName());
        userRequestDtoWithId = new UserRequestDto(user.getId(), user.getEmail(), user.getName());
    }

    @Test
    public void givenUserDto_whenCreate_thenStatus200andUserReturned() throws Exception {
        when(userService.create(any())).thenReturn(user);

        mvc.perform(post("/users")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(userRequestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())));
    }

    @Test
    public void givenUserDto_whenFindAll_thenStatus200andUserReturned() throws Exception {
        when(userService.findAll()).thenReturn(Collections.singletonList(user));

        mvc.perform(get("/users")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void givenUserDto_whenUpdate_thenStatus200andUserReturned() throws Exception {
        String updatedName = "updatedName";
        UserRequestDto updatedUser = new UserRequestDto();
        updatedUser.setName(updatedName);
        user.setName(updatedName);
        userRequestDtoWithId.setName(updatedName);

        when(userService.update(any(), any())).thenReturn(user);

        mvc.perform(patch("/users/{userId}", user.getId())
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(updatedUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("updatedName")));
    }

    @Test
    public void givenNotFoundObjectException_whenUpdate_thenStatus404andThrownNotFoundObjectException() throws Exception {
        String updatedName = "updatedName";
        UserRequestDto updatedUser = new UserRequestDto();
        updatedUser.setName(updatedName);
        user.setName(updatedName);
        userRequestDtoWithId.setName(updatedName);

        when(userService.update(any(), any())).thenThrow(NotFoundObjectException.class);

        mvc.perform(patch("/users/{userId}", user.getId())
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(updatedUser)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    public void givenUserDto_whenFindById_thenStatus200andUserReturned() throws Exception {
        when(userService.findById(any())).thenReturn(user);

        mvc.perform(get("/users/{userId}", user.getId())
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void givenNotFoundObjectException_whenFindById_thenStatus404andThrownNotFoundObjectException() throws Exception {
        when(userService.findById(any())).thenThrow(NotFoundObjectException.class);

        mvc.perform(get("/users/{userId}", user.getId())
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
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