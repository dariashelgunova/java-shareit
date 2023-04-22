package ru.practicum.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.exception.NotFoundObjectException;
import ru.practicum.item.model.Item;
import ru.practicum.request.dto.ItemRequestRequestDto;
import ru.practicum.request.dto.ItemRequestToReturnDto;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.request.service.ItemRequestService;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.request.mapper.ItemRequestMapper.toItemRequestToReturnDto;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @MockBean
    private ItemRequestService itemRequestService;
    @Autowired
    private MockMvc mvc;
    private ItemRequest request;
    private ItemRequestRequestDto itemRequestRequestDto;
    private ItemRequestToReturnDto itemRequestToReturnDto;

    @BeforeEach
    void setup() {
        mapper.registerModule(new JavaTimeModule());

        User owner = createUser(1L);
        User booker = createUser(2L);
        Item item = createItem(1L, owner);
        request = createItemRequest(1L, booker, item);

        itemRequestRequestDto = new ItemRequestRequestDto(null, request.getDescription());
        itemRequestToReturnDto = toItemRequestToReturnDto(request);
    }

    @Test
    public void givenRequestDto_whenCreate_thenStatus200andRequestReturned() throws Exception {
        when(itemRequestService.create(any(), any())).thenReturn(request);
        mvc.perform(post("/requests")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(itemRequestRequestDto))
                        .header("X-Sharer-User-Id", 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())));
    }

    @Test
    public void givenNotFoundObjectException_whenCreate_thenStatus404andThrownNotFoundObjectException() throws Exception {
        when(itemRequestService.create(any(), any())).thenThrow(NotFoundObjectException.class);
        mvc.perform(post("/requests")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(itemRequestRequestDto))
                        .header("X-Sharer-User-Id", 2))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    public void givenRequestDto_whenFindingRequestsCreatedByOtherUsers_thenStatus200andRequestReturned() throws Exception {
        List<ItemRequest> result = Collections.singletonList(request);

        when(itemRequestService.findRequestsCreatedByUsers(any(), any(), any())).thenReturn(result);

        mvc.perform(get("/requests/all")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void givenRequestDto_whenFindAll_thenStatus200andRequestListReturned() throws Exception {
        List<ItemRequest> result = Collections.singletonList(request);

        when(itemRequestService.findAll(any())).thenReturn(result);

        mvc.perform(get("/requests")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 2))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void givenRequestDto_whenFindingRequestsById_thenStatus200andRequestReturned() throws Exception {
        when(itemRequestService.findById(any(), any())).thenReturn(request);
        mvc.perform(get("/requests/{requestId}", request.getId())
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())));
    }

    @Test
    public void givenNotFoundObjectException_whenFindingRequestsById_thenStatus404andThrowNotFoundObjectException() throws Exception {
        when(itemRequestService.findById(any(), any())).thenThrow(NotFoundObjectException.class);
        mvc.perform(get("/requests/{requestId}", request.getId())
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 2))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }


    private User createUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setName("name" + id);
        user.setEmail("yandex" + id + "@yandex.ru");
        return user;
    }

    private Item createItem(Long id, User owner) {
        Item item = new Item();
        item.setId(id);
        item.setAvailable(true);
        item.setOwner(owner);
        item.setName("item" + id);
        item.setDescription("description" + id);
        return item;
    }

    private ItemRequest createItemRequest(Long id, User requestor, Item item) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(id);
        itemRequest.setDescription("description");
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestor(requestor);
        itemRequest.setItems(Collections.singletonList(item));

        return itemRequest;
    }
}