package ru.practicum.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.booking.dto.BookingDtoToReturn;
import ru.practicum.booking.dto.BookingRequestDto;
import ru.practicum.booking.dto.BookingSimpleDto;
import ru.practicum.booking.model.Booking;
import ru.practicum.item.mapper.ItemMapper;
import ru.practicum.item.model.Item;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

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

    public static BookingDtoToReturn toBookingDtoToReturn(Booking booking) {
        if (booking == null) return null;

        BookingDtoToReturn bookingDto = new BookingDtoToReturn();
        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setItem(ItemMapper.toItemSimpleDto(booking.getItem()));
        bookingDto.setBooker(UserMapper.toUserSimpleDto(booking.getBooker()));
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
}
