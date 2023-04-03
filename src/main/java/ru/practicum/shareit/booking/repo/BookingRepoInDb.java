package ru.practicum.shareit.booking.repo;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepoInDb extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdAndStartLessThanEqualAndEndGreaterThanEqual(Long bookerId, LocalDateTime start, LocalDateTime end, Sort sort);

    List<Booking> findByBookerIdAndStartGreaterThanEqual(Long bookerId, LocalDateTime start, Sort sort);

    List<Booking> findByBookerIdAndEndLessThanEqual(Long bookerId, LocalDateTime end, Sort sort);

    List<Booking> findByBookerIdAndStatus(Long bookerId, Status status, Sort sort);

    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findByItemOwnerIdAndStartLessThanEqualAndEndGreaterThanEqual(Long itemOwnerId, LocalDateTime start, LocalDateTime end, Sort sort);

    List<Booking> findByItemOwnerIdAndStartGreaterThanEqual(Long itemOwnerId, LocalDateTime start, Sort sort);

    List<Booking> findByItemOwnerIdAndEndLessThanEqual(Long itemOwnerId, LocalDateTime end, Sort sort);

    List<Booking> findByItemOwnerIdAndStatus(Long itemOwnerId, Status status, Sort sort);

    List<Booking> findByItemOwnerIdOrderByStartDesc(Long itemOwnerId);

    Booking findFirstByItemIdAndStartLessThanEqualAndStatus(Long itemId, LocalDateTime start, Status status, Sort sort);

    Booking findFirstByItemIdAndStartGreaterThanEqualAndStatus(Long itemId, LocalDateTime start, Status status, Sort sort);

    List<Booking> findByItemIdInAndStartLessThanEqualAndStatus(List<Long> itemId, LocalDateTime start, Status status, Sort sort);

    List<Booking> findByItemIdInAndStartGreaterThanEqualAndStatus(List<Long> itemId, LocalDateTime start, Status status, Sort sort);
}

