package ru.practicum.shareit.booking.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repo.BookingRepoInDb;
import ru.practicum.shareit.exception.ApproveBookingException;
import ru.practicum.shareit.exception.AvailabilityException;
import ru.practicum.shareit.exception.NotFoundObjectException;
import ru.practicum.shareit.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingServiceImpl implements BookingService {

    BookingRepoInDb bookingRepoInDb;

    @Transactional
    public Booking create(Booking booking, Long userId) {
        booking.setStatus(Status.WAITING);
        if (!booking.getItem().getAvailable()) {
            throw new AvailabilityException("Данная вещь недоступна для аренды!");
        } else if (booking.getEnd().isBefore(booking.getStart())) {
            throw new ValidationException("Дата окончания не может быть раньше даты начала!");
        } else if (booking.getEnd().equals(booking.getStart())) {
            throw new ValidationException("Дата окончания не может быть равна дате начала!");
        } else if (booking.getBooker().getId().equals(booking.getItem().getOwner().getId())) {
            throw new ApproveBookingException("Собственник не может взять в аренду свою же вещь!");
        } else {
            return bookingRepoInDb.save(booking);
        }
    }

    @Transactional
    public Booking acceptOrRejectRequest(boolean approved, Long userId, Long bookingId) {
        Booking booking = getBookingByIdOrThrowException(bookingId);
        if (Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            if (booking.getStatus().equals(Status.APPROVED)) {
                throw new AvailabilityException("Нельзя менять статус уже одобренной аренды!");
            } else if (approved) {
                booking.setStatus(Status.APPROVED);
            } else {
                booking.setStatus(Status.REJECTED);
            }
            //bookingRepoInDb.save(booking);
            return booking;
        } else {
            throw new NotFoundObjectException("Только собственник может менять статус бронирования!");
        }
    }

    public Booking findById(Long bookingId) {
        return getBookingByIdOrThrowException(bookingId);
    }

    @Override
    public void checkAccessForBookingByUserId(Booking booking, Long userId) {
        if (!userId.equals(booking.getItem().getOwner().getId()) &&
                !userId.equals(booking.getBooker().getId()))
            throw new NotFoundObjectException("Данный пользователь не имеет доступа к данному бронированию.");
    }

    public boolean checkAccessForItemLastNextBookingByUserId(Booking booking, Long userId) {
        if (booking == null) {
            return false;
        }
        return userId.equals(booking.getItem().getOwner().getId()) && booking.getStatus().equals(Status.APPROVED);
    }

    public List<Booking> findBookingsByUser(Long userId, State state) {
        List<Booking> result = getBookingsByStateForBooker(userId, state);
        if (result.isEmpty()) {
            throw new NotFoundObjectException("По вашему запросу ничего найдено.");
        } else {
            return result;
        }
    }

    public List<Booking> findBookingsByUserForComment(Long userId, State state) {
        List<Booking> result = getBookingsByStateForBooker(userId, state);
        if (result.isEmpty()) {
            throw new AvailabilityException("Нельзя оставить комментарий без бронивания.");
        } else {
            return result;
        }
    }

    public List<Booking> findBookingsByOwner(Long userId, State state) {
        List<Booking> result = getBookingsByStateForOwner(userId, state);
        if (result.isEmpty()) {
            throw new NotFoundObjectException("По вашему запросу ничего найдено.");
        } else {
            return result;
        }
    }

    private Booking getBookingByIdOrThrowException(Long bookingId) {
        return bookingRepoInDb.findById(bookingId)
                .orElseThrow(() -> new NotFoundObjectException("Объект не был найден"));
    }

    private List<Booking> getBookingsByStateForOwner(Long userId, State state) {
        List<Booking> result = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.from(LocalDateTime.now());
        Sort sort = Sort.by(Sort.Direction.DESC, "start");

        switch (state) {
            case CURRENT:
                result = bookingRepoInDb.findByItemOwnerIdAndStartLessThanEqualAndEndGreaterThanEqual(userId, currentTime, currentTime, sort);
                break;

            case FUTURE:
                result = bookingRepoInDb.findByItemOwnerIdAndStartGreaterThanEqual(userId, currentTime, sort);
                break;

            case PAST:
                result = bookingRepoInDb.findByItemOwnerIdAndEndLessThanEqual(userId, currentTime, sort);
                break;

            case REJECTED:
                result = bookingRepoInDb.findByItemOwnerIdAndStatus(userId, Status.REJECTED, sort);
                break;

            case WAITING:
                result = bookingRepoInDb.findByItemOwnerIdAndStatus(userId, Status.WAITING, sort);
                break;

            case ALL:
                result = bookingRepoInDb.findByItemOwnerIdOrderByStartDesc(userId);
                break;
        }
        return result;
    }

    private List<Booking> getBookingsByStateForBooker(Long userId, State state) {
        List<Booking> result = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.from(LocalDateTime.now());
        Sort sort = Sort.by(Sort.Direction.DESC, "start");

        switch (state) {
            case CURRENT:
                result = bookingRepoInDb.findByBookerIdAndStartLessThanEqualAndEndGreaterThanEqual(userId, currentTime, currentTime, sort);
                break;

            case FUTURE:
                result = bookingRepoInDb.findByBookerIdAndStartGreaterThanEqual(userId, currentTime, sort);
                break;

            case PAST:
                result = bookingRepoInDb.findByBookerIdAndEndLessThanEqual(userId, currentTime, sort);
                break;

            case REJECTED:
                result = bookingRepoInDb.findByBookerIdAndStatus(userId, Status.REJECTED, sort);
                break;

            case WAITING:
                result = bookingRepoInDb.findByBookerIdAndStatus(userId, Status.WAITING, sort);
                break;

            case ALL:
                result = bookingRepoInDb.findByBookerIdOrderByStartDesc(userId);
                break;
        }
        return result;
    }

    public Booking findLastBookingByItemId(Long itemId) {
        LocalDateTime currentTime = LocalDateTime.from(LocalDateTime.now());
        return bookingRepoInDb.findFirstByItemIdAndStartLessThanEqualAndStatus(itemId, currentTime, Status.APPROVED, Sort.by(Sort.Direction.DESC, "start"));
    }

    public Booking findNextBookingByItemId(Long itemId) {
        LocalDateTime currentTime = LocalDateTime.from(LocalDateTime.now());
        return bookingRepoInDb.findFirstByItemIdAndStartGreaterThanEqualAndStatus(itemId, currentTime, Status.APPROVED, Sort.by(Sort.Direction.ASC, "start"));
    }

    public List<Booking> findLastBookingByItemIds(List<Long> items) {
        LocalDateTime currentTime = LocalDateTime.from(LocalDateTime.now());
        return bookingRepoInDb.findByItemIdInAndStartLessThanEqualAndStatus(items, currentTime, Status.APPROVED, Sort.by(Sort.Direction.DESC, "start"));
    }

    public List<Booking> findNextBookingByItemIds(List<Long> items) {
        LocalDateTime currentTime = LocalDateTime.from(LocalDateTime.now());
        return bookingRepoInDb.findByItemIdInAndStartGreaterThanEqualAndStatus(items, currentTime, Status.APPROVED, Sort.by(Sort.Direction.ASC, "start"));
    }

}
