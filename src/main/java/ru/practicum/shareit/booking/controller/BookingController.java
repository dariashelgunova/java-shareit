package ru.practicum.shareit.booking.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoToReturn;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.booking.mapper.BookingMapper.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {
    BookingService bookingService;
    ItemService itemService;
    UserService userService;

    @PostMapping
    public BookingDtoToReturn create(@Valid @RequestBody BookingRequestDto booking,
                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        Booking newBooking = fromBookingRequestDto(booking, setItem(booking), setUser(userId));
        Booking createdBooking = bookingService.create(newBooking, userId);
        return toBookingDtoToReturn(createdBooking);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoToReturn acceptOrRejectRequest(@RequestParam boolean approved,
                                                    @RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @PathVariable("bookingId") Long bookingId) {
        Booking booking = bookingService.acceptOrRejectRequest(approved, userId, bookingId);
        return toBookingDtoToReturn(booking);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoToReturn findById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @PathVariable("bookingId") Long bookingId) {
        Booking booking = bookingService.findById(bookingId);
        bookingService.checkAccessForBookingByUserId(booking, userId);
        return toBookingDtoToReturn(booking);
    }

    @GetMapping
    public List<BookingDtoToReturn> findBookingsByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                       @RequestParam(defaultValue = "ALL") String state) {
        State stateEnum = State.findByValueOrThrowException(state);
        return toBookingDtoToReturnList(bookingService.findBookingsByUser(userId, stateEnum));
    }

    @GetMapping("/owner")
    public List<BookingDtoToReturn> findBookingsByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @RequestParam(defaultValue = "ALL") String state) {
        State stateEnum = State.findByValueOrThrowException(state);
        return toBookingDtoToReturnList(bookingService.findBookingsByOwner(userId, stateEnum));
    }

    private Item setItem(BookingRequestDto bookingRequestDto) {
        return itemService.findById(bookingRequestDto.getItemId());
    }

    private User setUser(Long userId) {
        return userService.findById(userId);
    }
}
