package ru.practicum.shareit.item.controller;

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
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repo.BookingRepo;
import ru.practicum.shareit.item.comment.dto.CommentDtoToReturn;
import ru.practicum.shareit.item.comment.dto.CommentRequestDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repo.CommentRepo;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;
import ru.practicum.shareit.item.dto.ItemDtoWithComments;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repo.db.UserRepo;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.item.comment.mapper.CommentMapper.toCommentSimpleDto;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    private final ObjectMapper mapper = new ObjectMapper();
    @Mock
    private UserRepo userRepo;
    @Mock
    private BookingRepo bookingRepo;
    @Mock
    private CommentRepo commentRepo;
    @Mock
    private ItemService itemService;
    @Mock
    private UserService userService;
    @Mock
    private ItemRequestService itemRequestService;
    @InjectMocks
    private ItemController itemController;
    private MockMvc mvc;
    private Item item;
    private ItemRequestDto itemRequestDto;
    private ItemDtoForOwner itemDtoForOwner;
    private ItemDtoWithComments itemDtoWithComments;
    private Comment comment;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .standaloneSetup(itemController)
                .build();
        mapper.registerModule(new JavaTimeModule());

        User owner = createUser(1L);
        when(userRepo.save(any())).thenReturn(owner);

        User booker = createUser(2L);
        lenient().when(userRepo.save(any())).thenReturn(booker);

        item = createItem(1L, owner);

        Booking booking = createBooking1(1L, item, booker);
        lenient().when(bookingRepo.save(any())).thenReturn(booking);

        comment = createComment(1L, item, booker);
        lenient().when(commentRepo.save(any())).thenReturn(comment);

        itemRequestDto = new ItemRequestDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), null);
        itemDtoForOwner = new ItemDtoForOwner(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), null, null, null);
        itemDtoWithComments = new ItemDtoWithComments(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), null);
    }

    @Test
    public void givenItemDto_whenCreate_thenStatus200andItemReturned() throws Exception {
        when(itemService.create(any(), any())).thenReturn(item);
        String response = mvc.perform(post("/items")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(mapper.writeValueAsString(itemRequestDto), response);
    }

    @Test
    public void givenItemDto_whenUpdate_thenStatus200andUpdatedItemReturned() throws Exception {
        String newDescription = "UpdatedDescription";
        ItemRequestDto updatedItem = new ItemRequestDto();
        updatedItem.setDescription(newDescription);
        item.setDescription(newDescription);
        itemRequestDto.setDescription(newDescription);

        when(itemService.update(any(), any(), any())).thenReturn(item);
        String response = mvc.perform(patch("/items/{itemId}", item.getId())
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(updatedItem))
                        .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(mapper.writeValueAsString(itemRequestDto), response);
    }

    @Test
    public void givenItemDto_whenFindingById_thenStatus200andItemReturned() throws Exception {
        itemDtoForOwner.setComments(Collections.singletonList(toCommentSimpleDto(comment)));

        when(itemService.findById(any())).thenReturn(item);
        item.setComments(Collections.singletonList(comment));
        when(itemService.addCommentsAndBookingsToItem(any(), any())).thenReturn(item);

        String response = mvc.perform(get("/items/{itemId}", item.getId())
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(mapper.writeValueAsString(itemDtoForOwner), response);
    }

    @Test
    public void givenItemDto_whenDeletingById_thenStatus200() throws Exception {
        String response = mvc.perform(delete("/items/{itemId}", item.getId())
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertTrue(response.isBlank());
    }

    @Test
    public void givenItemDto_whenDeletingAll_thenStatus200() throws Exception {
        String response = mvc.perform(delete("/items")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertTrue(response.isBlank());
    }

    @Test
    public void givenItemDto_whenFindingItemsByOwner_thenStatus200andItemListReturned() throws Exception {
        when(itemService.findItemsByOwner(any(), any(), any())).thenReturn(Collections.singletonList(item));
        when(itemService.addCommentsAndBookingsToItems(any(), any())).thenReturn(Collections.singletonList(item));
        String response = mvc.perform(get("/items")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(mapper.writeValueAsString(Collections.singletonList(itemDtoForOwner)), response);
    }

    @Test
    public void givenItemDto_whenFindingItemsBySearch_thenStatus200andItemListReturned() throws Exception {
        when(itemService.findItemsBySearch(any(), any(), any())).thenReturn(Collections.singletonList(item));
        String response = mvc.perform(get("/items/search")
                        .param("text", "description")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(mapper.writeValueAsString(Collections.singletonList(itemRequestDto)), response);
    }

    @Test
    public void givenItemDto_whenCreateComment_thenStatus200andItemReturned() throws Exception {
        LocalDateTime created = LocalDateTime.now();
        CommentRequestDto commentRequestDto = new CommentRequestDto(null, comment.getText());
        CommentDtoToReturn commentDtoToReturn = new CommentDtoToReturn(comment.getId(), comment.getText(), null, "name2", comment.getCreated());

        item.setComments(Collections.singletonList(comment));

        when(itemService.addComment(any(), any(), any())).thenReturn(comment);
        String response = mvc.perform(post("/items/{itemId}/comment", item.getId())
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(commentRequestDto))
                        .header("X-Sharer-User-Id", 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(mapper.writeValueAsString(commentDtoToReturn), response);
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

    private Booking createBooking1(Long id, Item item, User booker) {
        LocalDateTime start = LocalDateTime.now().minus(60, ChronoUnit.MINUTES);
        LocalDateTime end = start.plus(10, ChronoUnit.SECONDS);
        return new Booking(id, start, end, item, booker, Status.WAITING);
    }

    private Comment createComment(Long id, Item item, User author) {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setCreated(LocalDateTime.now());
        comment.setText("text");
        comment.setItem(item);
        comment.setAuthor(author);
        return comment;
    }
}