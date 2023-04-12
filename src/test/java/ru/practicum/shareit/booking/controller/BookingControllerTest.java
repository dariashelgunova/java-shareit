package ru.practicum.shareit.booking.controller;

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
import ru.practicum.shareit.booking.dto.BookingDtoToReturn;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.db.ItemRepo;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repo.db.UserRepo;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.booking.mapper.BookingMapper.toBookingDtoToReturn;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @Mock
    private BookingService bookingService;
    @Mock
    private ItemRepo itemRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private ItemService itemService;
    @Mock
    private UserService userService;
    @InjectMocks
    private BookingController bookingController;
    private MockMvc mvc;
    private Booking booking;
    private BookingRequestDto bookingRequestDto;
    private BookingDtoToReturn bookingDtoToReturn;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .standaloneSetup(bookingController)
                .build();

        mapper.registerModule(new JavaTimeModule());

        User owner = createUser(1L);
        when(userRepo.save(any())).thenReturn(owner);
        User booker = createUser(2L);
        lenient().when(userRepo.save(any())).thenReturn(booker);
        Item item = createItem(1L, owner);
        lenient().when(itemRepo.save(any())).thenReturn(item);

        booking = createBooking1(1L, item, booker);

        bookingRequestDto = new BookingRequestDto(null, booking.getStart(), booking.getEnd(), item.getId());
        bookingDtoToReturn = toBookingDtoToReturn(booking);
    }

    @Test
    public void givenBookingDto_whenCreate_thenStatus200andBookingReturned() throws Exception {
        when(bookingService.create(any(), any())).thenReturn(booking);
        String response = mvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(bookingRequestDto))
                        .header("X-Sharer-User-Id", 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(mapper.writeValueAsString(bookingDtoToReturn), response);
    }

    @Test
    public void givenBooking_whenAcceptOrRejectBooking_thenStatus200andBookingReturned() throws Exception {
        booking.setStatus(Status.APPROVED);
        bookingDtoToReturn.setStatus(Status.APPROVED);
        when(bookingService.acceptOrRejectRequest(eq(true), any(), any())).thenReturn(booking);
        String response = mvc.perform(patch("/bookings/{bookingId}", booking.getId())
                        .param("approved", "true")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(bookingRequestDto))
                        .header("X-Sharer-User-Id", 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(mapper.writeValueAsString(bookingDtoToReturn), response);
    }

    @Test
    public void givenBooking_whenFindingById_thenStatus200andBookingReturned() throws Exception {
        when(bookingService.findById(any())).thenReturn(booking);
        String response = mvc.perform(get("/bookings/{bookingId}", booking.getId())
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(mapper.writeValueAsString(bookingDtoToReturn), response);
    }

    @Test
    public void givenBooking_whenFindingByUser_thenStatus200andBookingReturned() throws Exception {
        when(bookingService.findBookingsByUser(any(), any(), any(), any())).thenReturn(Collections.singletonList(booking));
        String response = mvc.perform(get("/bookings")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(mapper.writeValueAsString(Collections.singletonList(bookingDtoToReturn)), response);
    }

    @Test
    public void givenBooking_whenFindingByOwner_thenStatus200andBookingReturned() throws Exception {
        when(bookingService.findBookingsByOwner(any(), any(), any(), any())).thenReturn(Collections.singletonList(booking));
        String response = mvc.perform(get("/bookings/owner")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(mapper.writeValueAsString(Collections.singletonList(bookingDtoToReturn)), response);
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
        LocalDateTime start = LocalDateTime.now().plus(60, ChronoUnit.MINUTES);
        LocalDateTime end = start.plus(10, ChronoUnit.SECONDS);
        return new Booking(id, start, end, item, booker, Status.WAITING);
    }
}