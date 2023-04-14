package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDtoToReturn;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.ApproveBookingException;
import ru.practicum.shareit.exception.AvailabilityException;
import ru.practicum.shareit.exception.NotFoundObjectException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.booking.mapper.BookingMapper.toBookingDtoToReturn;

@WebMvcTest(BookingController.class)
class BookingControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @MockBean
    private BookingService bookingService;
    @MockBean
    private ItemService itemService;
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;
    private Booking booking;
    private BookingRequestDto bookingRequestDto;
    private BookingDtoToReturn bookingDtoToReturn;

    @BeforeEach
    void setup() {
        mapper.registerModule(new JavaTimeModule());

        User owner = createUser(1L);
        User booker = createUser(2L);
        Item item = createItem(1L, owner);

        booking = createBooking1(1L, item, booker);

        bookingRequestDto = new BookingRequestDto(null, booking.getStart(), booking.getEnd(), item.getId());
        bookingDtoToReturn = toBookingDtoToReturn(booking);
    }

    @Test
    public void givenBookingDto_whenCreate_thenStatus200andBookingReturned() throws Exception {
        when(bookingService.create(any(), any())).thenReturn(booking);

        mvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(bookingRequestDto))
                        .header("X-Sharer-User-Id", 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())));
    }

    @Test
    public void givenThrownAvailabilityException_whenCreate_thenStatus400andThrowAvailabilityException() throws Exception {
        when(bookingService.create(any(), any())).thenThrow(AvailabilityException.class);

        mvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(bookingRequestDto))
                        .header("X-Sharer-User-Id", 2))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)));
    }

    @Test
    public void givenThrownValidationException_whenCreate_thenStatus400andThrowValidationException() throws Exception {
        when(bookingService.create(any(), any())).thenThrow(ValidationException.class);

        mvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(bookingRequestDto))
                        .header("X-Sharer-User-Id", 2))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)));
    }

    @Test
    public void givenThrownApproveBookingException_whenCreate_thenStatus404andThrowApproveBookingException() throws Exception {
        when(bookingService.create(any(), any())).thenThrow(ApproveBookingException.class);

        mvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(bookingRequestDto))
                        .header("X-Sharer-User-Id", 2))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    public void givenBooking_whenAcceptOrRejectBooking_thenStatus200andBookingReturned() throws Exception {
        booking.setStatus(Status.APPROVED);
        bookingDtoToReturn.setStatus(Status.APPROVED);
        when(bookingService.acceptOrRejectRequest(eq(true), any(), any())).thenReturn(booking);

        mvc.perform(patch("/bookings/{bookingId}", booking.getId())
                        .param("approved", "true")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(bookingRequestDto))
                        .header("X-Sharer-User-Id", 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("APPROVED")));
    }

    @Test
    public void givenAvailabilityException_whenAcceptOrRejectBooking_thenStatus400andThrownAvailabilityException() throws Exception {
        booking.setStatus(Status.APPROVED);
        bookingDtoToReturn.setStatus(Status.APPROVED);
        when(bookingService.acceptOrRejectRequest(eq(true), any(), any())).thenThrow(AvailabilityException.class);

        mvc.perform(patch("/bookings/{bookingId}", booking.getId())
                        .param("approved", "true")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(bookingRequestDto))
                        .header("X-Sharer-User-Id", 2))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)));
    }

    @Test
    public void givenNotFoundObjectException_whenAcceptOrRejectBooking_thenStatus404andThrownNotFoundObjectException() throws Exception {
        booking.setStatus(Status.APPROVED);
        bookingDtoToReturn.setStatus(Status.APPROVED);
        when(bookingService.acceptOrRejectRequest(eq(true), any(), any())).thenThrow(NotFoundObjectException.class);

        mvc.perform(patch("/bookings/{bookingId}", booking.getId())
                        .param("approved", "true")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(bookingRequestDto))
                        .header("X-Sharer-User-Id", 2))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    public void givenBooking_whenFindingById_thenStatus200andBookingReturned() throws Exception {
        when(bookingService.findById(any())).thenReturn(booking);

        mvc.perform(get("/bookings/{bookingId}", booking.getId())
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void givenNotFoundObjectException_whenFindingById_thenStatus404andThrownNotFoundObjectException() throws Exception {
        when(bookingService.findById(any())).thenThrow(NotFoundObjectException.class);

        mvc.perform(get("/bookings/{bookingId}", booking.getId())
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 2))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    public void givenBooking_whenFindingByUser_thenStatus200andBookingReturned() throws Exception {
        List<Booking> result = Collections.singletonList(booking);
        when(bookingService.findBookingsByUser(any(), any(), any(), any())).thenReturn(result);

        mvc.perform(get("/bookings")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 2))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void givenNotFoundObjectException_whenFindingByUser_thenStatus404andThrownNotFoundObjectException() throws Exception {
        when(bookingService.findBookingsByUser(any(), any(), any(), any())).thenThrow(NotFoundObjectException.class);

        mvc.perform(get("/bookings")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 2))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    public void givenBooking_whenFindingByOwner_thenStatus200andBookingReturned() throws Exception {
        List<Booking> result = Collections.singletonList(booking);
        when(bookingService.findBookingsByOwner(any(), any(), any(), any())).thenReturn(result);

        mvc.perform(get("/bookings/owner")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void givenNotFoundObjectException_whenFindingByOwner_thenStatus404andThrownNotFoundObjectException() throws Exception {
        when(bookingService.findBookingsByOwner(any(), any(), any(), any())).thenThrow(NotFoundObjectException.class);

        mvc.perform(get("/bookings/owner")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 1))
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

    private Booking createBooking1(Long id, Item item, User booker) {
        LocalDateTime start = LocalDateTime.now().plus(60, ChronoUnit.MINUTES);
        LocalDateTime end = start.plus(10, ChronoUnit.SECONDS);
        return new Booking(id, start, end, item, booker, Status.WAITING);
    }
}