package ru.practicum.booking.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.client.BookingClient;
import ru.practicum.booking.dto.BookingRequestDto;
import ru.practicum.status.State;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {
    BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody BookingRequestDto booking,
                                         @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingClient.create(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> acceptOrRejectRequest(@RequestParam boolean approved,
                                                        @RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @PathVariable("bookingId") Long bookingId) {
        return bookingClient.acceptOrRejectRequest(approved, userId, bookingId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @PathVariable("bookingId") Long bookingId) {
        return bookingClient.findById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> findBookingsByUser(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                     @Positive @RequestParam(defaultValue = "10") Integer size,
                                                     @RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @RequestParam(defaultValue = "ALL") String state) {
        State stateEnum = State.findByValueOrThrowException(state);
        return bookingClient.findBookingsByUser(userId, stateEnum, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findBookingsByOwner(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                      @Positive @RequestParam(defaultValue = "10") Integer size,
                                                      @RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @RequestParam(defaultValue = "ALL") String state) {
        State stateEnum = State.findByValueOrThrowException(state);
        return bookingClient.findBookingsByOwner(userId, stateEnum, from, size);
    }
}
