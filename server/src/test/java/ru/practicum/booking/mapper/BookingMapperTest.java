package ru.practicum.booking.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.booking.dto.BookingDtoToReturn;
import ru.practicum.booking.dto.BookingRequestDto;
import ru.practicum.booking.dto.BookingSimpleDto;
import ru.practicum.booking.model.Booking;
import ru.practicum.booking.model.Status;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.practicum.booking.mapper.BookingMapper.*;

class BookingMapperTest {

    @Test
    void fromBookingRequestDtoTest() {
        LocalDateTime start = LocalDateTime.of(2023, 4, 30, 10, 10, 10);
        LocalDateTime end = LocalDateTime.of(2023, 4, 30, 15, 10, 10);
        BookingRequestDto dto = new BookingRequestDto(1L, start, end, 1L);

        Booking booking = fromBookingRequestDto(dto, null, null);

        assertEquals(dto.getId(), booking.getId());
        assertEquals(dto.getStart(), booking.getStart());
        assertEquals(dto.getEnd(), booking.getEnd());

        dto = null;
        assertNull(fromBookingRequestDto(dto, null, null));
    }

    @Test
    void toBookingDtoToReturnTest() {
        LocalDateTime start = LocalDateTime.of(2023, 4, 30, 10, 10, 10);
        LocalDateTime end = LocalDateTime.of(2023, 4, 30, 15, 10, 10);
        Booking booking = new Booking(1L, start, end, null, null, Status.APPROVED);

        BookingDtoToReturn dto = toBookingDtoToReturn(booking);

        assertEquals(dto.getId(), booking.getId());
        assertEquals(dto.getStart(), booking.getStart());
        assertEquals(dto.getEnd(), booking.getEnd());
        assertEquals(dto.getStatus(), booking.getStatus());

        booking = null;
        assertNull(toBookingDtoToReturn(booking));
    }

    @Test
    void toBookingDtoToReturnListTest() {
        LocalDateTime start = LocalDateTime.of(2023, 4, 30, 10, 10, 10);
        LocalDateTime end = LocalDateTime.of(2023, 4, 30, 15, 10, 10);
        Booking booking = new Booking(1L, start, end, null, null, Status.APPROVED);
        List<Booking> bookings = List.of(booking);

        List<BookingDtoToReturn> dtos = toBookingDtoToReturnList(bookings);
        assertEquals(bookings.size(), dtos.size());

        bookings = null;
        assertNull(toBookingDtoToReturnList(bookings));
    }

    @Test
    void toBookingSimpleDtoTest() {
        LocalDateTime start = LocalDateTime.of(2023, 4, 30, 10, 10, 10);
        LocalDateTime end = LocalDateTime.of(2023, 4, 30, 15, 10, 10);
        User booker = new User(1L, "yandex@yandex.ru", "name");
        Booking booking = new Booking(1L, start, end, null, booker, Status.APPROVED);

        BookingSimpleDto dto = toBookingSimpleDto(booking);
        assertEquals(dto.getId(), booking.getId());
        assertEquals(dto.getBookerId(), booking.getBooker().getId());

        booking = null;
        assertNull(toBookingSimpleDto(booking));
    }
}