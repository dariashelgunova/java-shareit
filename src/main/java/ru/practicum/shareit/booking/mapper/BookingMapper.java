package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDtoToReturn;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingSimpleDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.mapper.ItemMapper.toItemSimpleDto;
import static ru.practicum.shareit.user.mapper.UserMapper.toUserSimpleDto;

@UtilityClass
public class BookingMapper {

    public static Booking fromBookingRequestDto(BookingRequestDto bookingRequestDto, Item item, User user) {
        if (bookingRequestDto == null) return null;

        Booking booking = new Booking();
        booking.setId(bookingRequestDto.getId());
        booking.setStart(bookingRequestDto.getStart());
        booking.setEnd(bookingRequestDto.getEnd());
        booking.setItem(item);
        booking.setBooker(user);

        return booking;
    }

    public static BookingRequestDto toBookingRequestDto(Booking booking) {
        if (booking == null) return null;

        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setId(booking.getId());
        bookingRequestDto.setStart(booking.getStart());
        bookingRequestDto.setEnd(booking.getEnd());
        bookingRequestDto.setItemId(booking.getItem().getId());

        return bookingRequestDto;
    }

    public static List<BookingRequestDto> toBookingRequestDtoList(List<Booking> bookings) {
        if (bookings == null) return null;

        return bookings
                .stream()
                .map(BookingMapper::toBookingRequestDto)
                .collect(Collectors.toList());
    }

    public static BookingDtoToReturn toBookingDtoToReturn(Booking booking) {
        if (booking == null) return null;

        BookingDtoToReturn bookingDto = new BookingDtoToReturn();
        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setItem(toItemSimpleDto(booking.getItem()));
        bookingDto.setBooker(toUserSimpleDto(booking.getBooker()));
        bookingDto.setStatus(booking.getStatus());

        return bookingDto;
    }

    public static List<BookingDtoToReturn> toBookingDtoToReturnList(List<Booking> bookings) {
        if (bookings == null) return null;

        return bookings
                .stream()
                .map(BookingMapper::toBookingDtoToReturn)
                .collect(Collectors.toList());
    }

    public static BookingSimpleDto toBookingSimpleDto(Booking booking) {
        if (booking == null) return null;

        BookingSimpleDto bookingDto = new BookingSimpleDto();
        bookingDto.setId(booking.getId());
        bookingDto.setBookerId(booking.getBooker().getId());

        return bookingDto;
    }

    public static List<BookingSimpleDto> toBookingSimpleDtoList(List<Booking> bookings) {
        if (bookings == null) return null;

        return bookings
                .stream()
                .map(BookingMapper::toBookingSimpleDto)
                .collect(Collectors.toList());
    }
}
