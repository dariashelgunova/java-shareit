package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {
    Booking create(Booking booking, Long userId);

    Booking acceptOrRejectRequest(boolean approved, Long userId, Long bookingId);

    Booking findById(Long bookingId);

    void checkAccessForBookingByUserId(Booking booking, Long userId);

    boolean checkAccessForItemLastNextBookingByUserId(Booking booking, Long userId);

    List<Booking> findBookingsByUser(Long userId, State state);

    List<Booking> findBookingsByUserForComment(Long userId, State state, Long itemId);

    List<Booking> findBookingsByOwner(Long userId, State state);

    Booking findLastBookingByItemId(Long itemId);

    Booking findNextBookingByItemId(Long itemId);

    List<Booking> findLastBookingByItemIds(List<Long> items);

    List<Booking> findNextBookingByItemIds(List<Long> items);
}

