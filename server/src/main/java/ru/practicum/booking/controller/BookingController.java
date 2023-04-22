package ru.practicum.booking.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.dto.BookingDtoToReturn;
import ru.practicum.booking.dto.BookingRequestDto;
import ru.practicum.booking.mapper.BookingMapper;
import ru.practicum.booking.model.Booking;
import ru.practicum.booking.model.State;
import ru.practicum.booking.service.BookingService;
import ru.practicum.item.model.Item;
import ru.practicum.item.service.ItemService;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {
    BookingService bookingService;
    ItemService itemService;
    UserService userService;

    @PostMapping
    public BookingDtoToReturn create(@RequestBody BookingRequestDto booking,
                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        Booking newBooking = BookingMapper.fromBookingRequestDto(booking, setItem(booking), setUser(userId));
        Booking createdBooking = bookingService.create(newBooking, userId);
        return BookingMapper.toBookingDtoToReturn(createdBooking);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoToReturn acceptOrRejectRequest(@RequestParam boolean approved,
                                                    @RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @PathVariable("bookingId") Long bookingId) {
        Booking booking = bookingService.acceptOrRejectRequest(approved, userId, bookingId);
        return BookingMapper.toBookingDtoToReturn(booking);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoToReturn findById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @PathVariable("bookingId") Long bookingId) {
        Booking booking = bookingService.findById(bookingId);
        bookingService.checkAccessForBookingByUserId(booking, userId);
        return BookingMapper.toBookingDtoToReturn(booking);
    }

    @GetMapping
    public List<BookingDtoToReturn> findBookingsByUser(@RequestParam(defaultValue = "0") Integer from,
                                                       @RequestParam(defaultValue = "10") Integer size,
                                                       @RequestHeader("X-Sharer-User-Id") Long userId,
                                                       @RequestParam(defaultValue = "ALL") String state) {
        State stateEnum = State.findByValueOrThrowException(state);
        return BookingMapper.toBookingDtoToReturnList(bookingService.findBookingsByUser(userId, stateEnum, from, size));
    }

    @GetMapping("/owner")
    public List<BookingDtoToReturn> findBookingsByOwner(@RequestParam(defaultValue = "0") Integer from,
                                                        @RequestParam(defaultValue = "10") Integer size,
                                                        @RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @RequestParam(defaultValue = "ALL") String state) {
        State stateEnum = State.findByValueOrThrowException(state);
        return BookingMapper.toBookingDtoToReturnList(bookingService.findBookingsByOwner(userId, stateEnum, from, size));
    }

    private Item setItem(BookingRequestDto bookingRequestDto) {
        return itemService.findById(bookingRequestDto.getItemId());
    }

    private User setUser(Long userId) {
        return userService.findById(userId);
    }
}
