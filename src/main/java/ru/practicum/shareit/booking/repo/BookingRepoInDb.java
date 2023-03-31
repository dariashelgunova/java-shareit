package ru.practicum.shareit.booking.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepoInDb extends JpaRepository<Booking, Long> {

    List<Booking> findByStartBeforeAndEndAfterOrderByStartDesc(LocalDateTime start, LocalDateTime end);

    List<Booking> findByStartAfterOrderByStartDesc(LocalDateTime start);

    List<Booking> findByEndBeforeOrderByStartDesc(LocalDateTime end);

    List<Booking> findByStatusOrderByStartDesc(Status status);

    List<Booking> findByOrderByStartDesc();

    Booking findFirstByItemIdAndStartBeforeOrderByStartDesc(Long itemId, LocalDateTime start);

    Booking findFirstByItemIdAndStartAfterOrderByStartAsc(Long itemId, LocalDateTime start);

}

