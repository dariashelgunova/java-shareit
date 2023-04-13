package ru.practicum.shareit.request.controller;

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
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.ItemRepo;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestToReturnDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repo.UserRepo;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.request.mapper.ItemRequestMapper.toItemRequestToReturnDto;
import static ru.practicum.shareit.request.mapper.ItemRequestMapper.toItemRequestToReturnDtoList;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @InjectMocks
    private ItemRequestController itemRequestController;
    @Mock
    private ItemRequestService itemRequestService;
    @Mock
    private UserRepo userRepo;
    @Mock
    private ItemRepo itemRepo;
    private MockMvc mvc;
    private ItemRequest request;
    private ItemRequestRequestDto itemRequestRequestDto;
    private ItemRequestToReturnDto itemRequestToReturnDto;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .standaloneSetup(itemRequestController)
                .build();
        mapper.registerModule(new JavaTimeModule());

        User owner = createUser(1L);
        when(userRepo.save(any())).thenReturn(owner);

        User booker = createUser(2L);
        lenient().when(userRepo.save(any())).thenReturn(booker);

        Item item = createItem(1L, owner);
        lenient().when(itemRepo.save(any())).thenReturn(item);

        request = createItemRequest(1L, booker, item);

        itemRequestRequestDto = new ItemRequestRequestDto(null, request.getDescription());
        itemRequestToReturnDto = toItemRequestToReturnDto(request);
    }

    @Test
    public void givenRequestDto_whenCreate_thenStatus200andRequestReturned() throws Exception {
        when(itemRequestService.create(any(), any())).thenReturn(request);
        String response = mvc.perform(post("/requests")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(itemRequestRequestDto))
                        .header("X-Sharer-User-Id", 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(mapper.writeValueAsString(itemRequestToReturnDto), response);
    }

    @Test
    public void givenRequestDto_whenFindingRequestsCreatedByOtherUsers_thenStatus200andRequestReturned() throws Exception {
        List<ItemRequest> result = Collections.singletonList(request);

        when(itemRequestService.findRequestsCreatedByUsers(any(), any(), any())).thenReturn(result);
        String response = mvc.perform(get("/requests/all")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(mapper.writeValueAsString(toItemRequestToReturnDtoList(result)), response);
    }

    @Test
    public void givenRequestDto_whenFindAll_thenStatus200andRequestListReturned() throws Exception {
        List<ItemRequest> result = Collections.singletonList(request);

        when(itemRequestService.findAll(any())).thenReturn(result);
        String response = mvc.perform(get("/requests")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(mapper.writeValueAsString(toItemRequestToReturnDtoList(result)), response);
    }

    @Test
    public void givenRequestDto_whenFindingRequestsById_thenStatus200andRequestReturned() throws Exception {
        when(itemRequestService.findById(any(), any())).thenReturn(request);
        String response = mvc.perform(get("/requests/{requestId}", request.getId())
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(mapper.writeValueAsString(itemRequestToReturnDto), response);
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