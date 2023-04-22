package ru.practicum.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.booking.model.Booking;
import ru.practicum.booking.model.Status;
import ru.practicum.exception.AccessDeniedException;
import ru.practicum.exception.AvailabilityException;
import ru.practicum.exception.NotFoundObjectException;
import ru.practicum.item.comment.dto.CommentDtoToReturn;
import ru.practicum.item.comment.dto.CommentRequestDto;
import ru.practicum.item.comment.dto.CommentSimpleDto;
import ru.practicum.item.comment.model.Comment;
import ru.practicum.item.dto.ItemDtoForOwner;
import ru.practicum.item.dto.ItemRequestDto;
import ru.practicum.item.model.Item;
import ru.practicum.item.service.ItemService;
import ru.practicum.request.service.ItemRequestService;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.item.comment.mapper.CommentMapper.toCommentSimpleDtoList;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    private final ObjectMapper mapper = new ObjectMapper();
    @MockBean
    private ItemService itemService;
    @MockBean
    private UserService userService;
    @MockBean
    private ItemRequestService itemRequestService;
    @Autowired
    private MockMvc mvc;
    private Item item;
    private ItemRequestDto itemRequestDto;
    private ItemDtoForOwner itemDtoForOwner;
    private Comment comment;

    @BeforeEach
    void setup() {
        mapper.registerModule(new JavaTimeModule());

        User owner = createUser(1L);
        User booker = createUser(2L);
        item = createItem(1L, owner);
        Booking booking = createBooking1(1L, item, booker);
        comment = createComment(1L, item, booker);

        itemRequestDto = new ItemRequestDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), null);
        itemDtoForOwner = new ItemDtoForOwner(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), null, null, null);
    }

    @Test
    public void givenItemDto_whenCreate_thenStatus200andItemReturned() throws Exception {
        when(itemService.create(any(), any())).thenReturn(item);

        mvc.perform(post("/items")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())));
    }

    @Test
    public void givenNotFoundObjectException_whenCreate_thenStatus404andThrownNotFoundObjectException() throws Exception {
        when(itemService.create(any(), any())).thenThrow(NotFoundObjectException.class);

        mvc.perform(post("/items")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    public void givenItemDto_whenUpdate_thenStatus200andUpdatedItemReturned() throws Exception {
        String newDescription = "UpdatedDescription";
        ItemRequestDto updatedItem = new ItemRequestDto();
        updatedItem.setDescription(newDescription);
        item.setDescription(newDescription);
        itemRequestDto.setDescription(newDescription);

        when(itemService.update(any(), any(), any())).thenReturn(item);

        mvc.perform(patch("/items/{itemId}", item.getId())
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(updatedItem))
                        .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("UpdatedDescription")));
    }

    @Test
    public void givenAccessDeniedException_whenUpdate_thenStatus403andThrownAccessDeniedException() throws Exception {
        String newDescription = "UpdatedDescription";
        ItemRequestDto updatedItem = new ItemRequestDto();
        updatedItem.setDescription(newDescription);
        item.setDescription(newDescription);
        itemRequestDto.setDescription(newDescription);

        when(itemService.update(any(), any(), any())).thenThrow(AccessDeniedException.class);

        mvc.perform(patch("/items/{itemId}", item.getId())
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(updatedItem))
                        .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status", is(403)));
    }

    @Test
    public void givenNotFoundObjectException_whenUpdate_thenStatus404andThrownNotFoundObjectException() throws Exception {
        String newDescription = "UpdatedDescription";
        ItemRequestDto updatedItem = new ItemRequestDto();
        updatedItem.setDescription(newDescription);
        item.setDescription(newDescription);
        itemRequestDto.setDescription(newDescription);

        when(itemService.update(any(), any(), any())).thenThrow(NotFoundObjectException.class);

        mvc.perform(patch("/items/{itemId}", item.getId())
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(updatedItem))
                        .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    public void givenItemDto_whenFindingById_thenStatus200andItemReturned() throws Exception {
        List<CommentSimpleDto> result = toCommentSimpleDtoList(Collections.singletonList(comment));
        itemDtoForOwner.setComments(result);

        when(itemService.findById(any())).thenReturn(item);
        item.setComments(Collections.singletonList(comment));
        when(itemService.addCommentsAndBookingsToItem(any(), any())).thenReturn(item);

        mvc.perform(get("/items/{itemId}", item.getId())
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void givenNotFoundObjectException_whenFindingById_thenStatus404andThrownNotFoundObjectException() throws Exception {
        List<CommentSimpleDto> result = toCommentSimpleDtoList(Collections.singletonList(comment));
        itemDtoForOwner.setComments(result);

        when(itemService.findById(any())).thenReturn(item);
        item.setComments(Collections.singletonList(comment));
        when(itemService.addCommentsAndBookingsToItem(any(), any())).thenThrow(NotFoundObjectException.class);

        mvc.perform(get("/items/{itemId}", item.getId())
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
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
        List<Item> result = Collections.singletonList(item);

        when(itemService.findItemsByOwner(any(), any(), any())).thenReturn(result);
        when(itemService.addCommentsAndBookingsToItems(any(), any())).thenReturn(Collections.singletonList(item));

        mvc.perform(get("/items")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void givenNotFoundObjectException_whenFindingItemsByOwner_thenStatus404andThrownNotFoundObjectException() throws Exception {
        when(itemService.findItemsByOwner(any(), any(), any())).thenThrow(NotFoundObjectException.class);

        mvc.perform(get("/items")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    public void givenItemDto_whenFindingItemsBySearch_thenStatus200andItemListReturned() throws Exception {
        when(itemService.findItemsBySearch(any(), any(), any())).thenReturn(Collections.singletonList(item));

        mvc.perform(get("/items/search")
                        .param("text", "description")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void givenNotFoundObjectException_whenFindingItemsBySearch_thenStatus404andThrownNotFoundObjectException() throws Exception {
        when(itemService.findItemsBySearch(any(), any(), any())).thenThrow(NotFoundObjectException.class);

        mvc.perform(get("/items/search")
                        .param("text", "description")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    public void givenItemDto_whenCreateComment_thenStatus200andItemReturned() throws Exception {
        LocalDateTime created = LocalDateTime.now();
        CommentRequestDto commentRequestDto = new CommentRequestDto(null, comment.getText());
        CommentDtoToReturn commentDtoToReturn = new CommentDtoToReturn(comment.getId(), comment.getText(), null, "name2", comment.getCreated());

        item.setComments(Collections.singletonList(comment));

        when(itemService.addComment(any(), any(), any())).thenReturn(comment);

        mvc.perform(post("/items/{itemId}/comment", item.getId())
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(commentRequestDto))
                        .header("X-Sharer-User-Id", 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())));
    }

    @Test
    public void givenAvailabilityException_whenCreateComment_thenStatus400andThrowAvailabilityException() throws Exception {
        LocalDateTime created = LocalDateTime.now();
        CommentRequestDto commentRequestDto = new CommentRequestDto(null, comment.getText());
        CommentDtoToReturn commentDtoToReturn = new CommentDtoToReturn(comment.getId(), comment.getText(), null, "name2", comment.getCreated());

        item.setComments(Collections.singletonList(comment));

        when(itemService.addComment(any(), any(), any())).thenThrow(AvailabilityException.class);

        mvc.perform(post("/items/{itemId}/comment", item.getId())
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(commentRequestDto))
                        .header("X-Sharer-User-Id", 2))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)));
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