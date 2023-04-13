package ru.practicum.shareit.booking.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepo extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdAndStartLessThanEqualAndEndGreaterThanEqual(Long bookerId, LocalDateTime start, LocalDateTime end, Pageable pageRequest);

    List<Booking> findByBookerIdAndStartGreaterThanEqual(Long bookerId, LocalDateTime start, Pageable pageRequest);

    List<Booking> findByBookerIdAndEndLessThanEqual(Long bookerId, LocalDateTime end, Pageable pageRequest);

    List<Booking> findByBookerIdAndStatus(Long bookerId, Status status, Pageable pageRequest);

    List<Booking> findByBookerId(Long bookerId, Pageable pageRequest);

    List<Booking> findByItemOwnerIdAndStartLessThanEqualAndEndGreaterThanEqual(Long itemOwnerId, LocalDateTime start, LocalDateTime end, Pageable pageRequest);

    List<Booking> findByItemOwnerIdAndStartGreaterThanEqual(Long itemOwnerId, LocalDateTime start, Pageable pageRequest);

    List<Booking> findByItemOwnerIdAndEndLessThanEqual(Long itemOwnerId, LocalDateTime end, Pageable pageRequest);

    List<Booking> findByItemOwnerIdAndStatus(Long itemOwnerId, Status status, Pageable pageRequest);

    List<Booking> findByItemOwnerId(Long itemOwnerId, Pageable pageRequest);



    Booking findFirstByItemIdAndStartLessThanEqualAndStatus(Long itemId, LocalDateTime start, Status status, Sort sort);

    Booking findFirstByItemIdAndStartAfterAndStatus(Long itemId, LocalDateTime start, Status status, Sort sort);

    List<Booking> findByItemIdInAndStartLessThanEqualAndStatus(List<Long> itemId, LocalDateTime start, Status status, Sort sort);

    List<Booking> findByItemIdInAndStartAfterAndStatus(List<Long> itemId, LocalDateTime start, Status status, Sort sort);

    List<Booking> findByItemIdAndBookerIdAndEndLessThanEqualAndStatus(Long itemId, Long bookerId, LocalDateTime currentTime, Sort sort, Status status);

}

