package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoToReturn;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookingMapper {
    public Booking fromDtoToBooking(BookingDto bookingDto, Item item, User user) {
        if (bookingDto == null) return null;

        Booking booking = new Booking();
        booking.setId(bookingDto.getId());
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(bookingDto.getStatus());

        return booking;
    }

    public BookingDto fromBookingToDto(Booking booking) {
        if (booking == null) return null;

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setItemId(booking.getItem().getId());
        bookingDto.setBookerId(booking.getBooker().getId());
        bookingDto.setStatus(booking.getStatus());

        return bookingDto;
    }

    public List<BookingDto> mapToDtoList(List<Booking> bookings) {
        if (bookings == null) return null;

        return bookings
                .stream()
                .map(this::fromBookingToDto)
                .collect(Collectors.toList());
    }


    public BookingDtoToReturn fromBookingToDtoToReturn(Booking booking) {
        if (booking == null) return null;

        BookingDtoToReturn bookingDto = new BookingDtoToReturn();
        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setItem(booking.getItem());
        bookingDto.setBooker(booking.getBooker());
        bookingDto.setStatus(booking.getStatus());

        return bookingDto;
    }

    public List<BookingDtoToReturn> mapToDtoToReturnList(List<Booking> bookings) {
        if (bookings == null) return null;

        return bookings
                .stream()
                .map(this::fromBookingToDtoToReturn)
                .collect(Collectors.toList());
    }
}
