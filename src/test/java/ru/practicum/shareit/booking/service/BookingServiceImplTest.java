package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repo.BookingRepo;
import ru.practicum.shareit.exception.ApproveBookingException;
import ru.practicum.shareit.exception.AvailabilityException;
import ru.practicum.shareit.exception.NotFoundObjectException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.paginator.Paginator;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @InjectMocks
    BookingServiceImpl service;
    @Mock
    Paginator<Booking> paginator;
    @Mock
    BookingRepo bookingRepo;

    @Test
    public void givenBookingAndUserId_whenBookingItemNotAvailable_thenThrowAvailabilityException() {
        User user = createUserWithId(1L);
        Item item = createItemWithId(1L, 1L);
        item.setAvailable(false);
        Booking booking = createBooking(1L, item, user);

        assertThrows(AvailabilityException.class, () -> service.create(booking, 1L));
    }

    @Test
    public void givenBookingAndUserId_whenBookingEndBeforeStart_thenThrowValidationException() {
        User user = createUserWithId(1L);
        Item item = createItemWithId(1L, 1L);
        Booking booking = createBooking(1L, item, user);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().minus(5, ChronoUnit.SECONDS));

        assertThrows(ValidationException.class, () -> service.create(booking, 1L));
    }

    @Test
    public void givenBookingAndUserId_whenBookingEndEqualsStart_thenThrowValidationException() {
        User user = createUserWithId(1L);
        Item item = createItemWithId(1L, 1L);
        Booking booking = createBooking(1L, item, user);
        LocalDateTime currentTime = LocalDateTime.now();
        booking.setStart(currentTime);
        booking.setEnd(currentTime);

        assertThrows(ValidationException.class, () -> service.create(booking, 1L));
    }

    @Test
    public void givenBookingAndUserId_whenBookingOwnerIdEndEqualsBookerId_thenThrowApproveBookingException() {
        User user = createUserWithId(1L);
        Item item = createItemWithId(1L, 1L);
        Booking booking = createBooking(1L, item, user);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plus(10, ChronoUnit.SECONDS));

        assertThrows(ApproveBookingException.class, () -> service.create(booking, 1L));
    }

    @Test
    public void givenBookingAndUserId_whenNothing_thenInvokeBookingRepo() {
        User user = createUserWithId(1L);
        Item item = createItemWithId(1L, 2L);
        Booking booking = createBooking(1L, item, user);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plus(10, ChronoUnit.SECONDS));

        service.create(booking, 1L);

        verify(bookingRepo, times(1)).save(booking);
    }

    @Test
    public void givenApprovedUserIdBookingId_whenObjectNotFound_thenThrowNotFoundObjectException() {
        when(bookingRepo.findById(any())).thenThrow(NotFoundObjectException.class);

        assertThrows(NotFoundObjectException.class, () -> service.acceptOrRejectRequest(true, 1L, 1L));
    }

    @Test
    public void givenApprovedUserIdBookingId_whenAcceptingNotByOwner_thenThrowNotFoundObjectException() {
        boolean approved = true;
        long userId = 1L;
        long bookingId = 1L;
        Booking booking = createBooking(bookingId, createItemWithId(1L, 2L), createUserWithId(3L));

        when(bookingRepo.findById(any())).thenReturn(Optional.of(booking));

        assertThrows(NotFoundObjectException.class, () -> service.acceptOrRejectRequest(approved, userId, 1L));
    }

    @Test
    public void givenApprovedUserIdBookingId_whenBookingAlreadyAccepted_thenThrowAvailabilityException() {
        boolean approved = true;
        long userId = 2L;
        long bookingId = 1L;
        Booking booking = createBooking(bookingId, createItemWithId(1L, 2L), createUserWithId(3L));
        booking.setStatus(Status.APPROVED);

        when(bookingRepo.findById(any())).thenReturn(Optional.of(booking));

        assertThrows(AvailabilityException.class, () -> service.acceptOrRejectRequest(approved, userId, 1L));
    }

    @Test
    public void givenApprovedUserIdBookingId_whenApproving_thenReturnBookingWithApprovedStatus() {
        boolean approved = true;
        long userId = 2L;
        long bookingId = 1L;
        Booking booking = createBooking(bookingId, createItemWithId(1L, 2L), createUserWithId(3L));
        booking.setStatus(Status.WAITING);

        when(bookingRepo.findById(any())).thenReturn(Optional.of(booking));

        Booking result = service.acceptOrRejectRequest(approved, userId, bookingId);
        assertEquals(Status.APPROVED, result.getStatus());
    }

    @Test
    public void givenApprovedUserIdBookingId_whenRejecting_thenReturnBookingWithRejectedStatus() {
        boolean approved = false;
        long userId = 2L;
        long bookingId = 1L;
        Booking booking = createBooking(bookingId, createItemWithId(1L, 2L), createUserWithId(3L));
        booking.setStatus(Status.WAITING);

        when(bookingRepo.findById(any())).thenReturn(Optional.of(booking));

        Booking result = service.acceptOrRejectRequest(approved, userId, bookingId);
        assertEquals(Status.REJECTED, result.getStatus());
    }

    @Test
    public void givenBookingId_whenFindingById_thenReturnBooking() {
        long bookingId = 1L;
        Booking booking = createBooking(bookingId, createItemWithId(1L, 2L), createUserWithId(3L));
        when(bookingRepo.findById(any())).thenReturn(Optional.of(booking));

        assertEquals(booking, service.findById(1L));
    }

    @Test
    public void givenBookingId_whenNoBookingFound_thenThrowNotFoundObjectException() {
        when(bookingRepo.findById(any())).thenThrow(NotFoundObjectException.class);

        assertThrows(NotFoundObjectException.class, () -> service.findById(1L));
    }

    @Test
    public void givenBookingAndUserId_whenCheckingAccess_thenThrowNotFoundObjectException() {
        long userId = 5L;
        long bookingId = 1L;
        Booking booking = createBooking(bookingId, createItemWithId(1L, 2L), createUserWithId(3L));

        assertThrows(NotFoundObjectException.class, () -> service.checkAccessForBookingByUserId(booking, userId));
    }

    @Test
    public void givenBookingAndUserId_whenCheckingAccess_thenDoesNotThrowNotFoundObjectException() {
        long userId = 2L;
        long bookingId = 1L;
        Booking booking = createBooking(bookingId, createItemWithId(1L, 2L), createUserWithId(3L));

        assertDoesNotThrow(() -> service.checkAccessForBookingByUserId(booking, userId));
    }

    @Test
    public void givenBookingAndUserId_whenBookingNull_thenReturnFalse() {
        assertFalse(service.checkAccessForItemLastNextBookingByUserId(null, 1L));
    }

    @Test
    public void givenBookingAndUserId_whenStatusNotApproved_thenReturnFalse() {
        long userId = 2L;
        long bookingId = 1L;
        Booking booking = createBooking(bookingId, createItemWithId(1L, 2L), createUserWithId(3L));
        booking.setStatus(Status.REJECTED);

        assertFalse(service.checkAccessForItemLastNextBookingByUserId(booking, userId));
    }

    @Test
    public void givenBookingAndUserId_whenuserIdDoesNotEqualOwnerId_thenReturnFalse() {
        long userId = 5L;
        long bookingId = 1L;
        Booking booking = createBooking(bookingId, createItemWithId(1L, 2L), createUserWithId(3L));
        booking.setStatus(Status.APPROVED);

        assertFalse(service.checkAccessForItemLastNextBookingByUserId(booking, userId));
    }

    @Test
    public void givenBookingAndUserId_whenAccess_thenReturnTrue() {
        long userId = 2L;
        long bookingId = 1L;
        Booking booking = createBooking(bookingId, createItemWithId(1L, 2L), createUserWithId(3L));
        booking.setStatus(Status.APPROVED);

        assertTrue(service.checkAccessForItemLastNextBookingByUserId(booking, userId));
    }

    @Test
    public void givenUserId_whenNoObjectsFoundAndCurrentState_thenThrowNotFoundObjectException() {
        when(bookingRepo.findByBookerIdAndStartLessThanEqualAndEndGreaterThanEqual(any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());
        assertThrows(NotFoundObjectException.class, () -> service.findBookingsByUser(1L, State.CURRENT, -1, -1));
    }

    @Test
    public void givenUserId_whenCurrentState_thenReturnList() {
        long bookingId = 1L;
        Booking booking = createBooking(bookingId, createItemWithId(1L, 2L), createUserWithId(3L));

        List<Booking> expected = List.of(booking);
        when(bookingRepo.findByBookerIdAndStartLessThanEqualAndEndGreaterThanEqual(any(), any(), any(), any()))
                .thenReturn(expected);
        when(paginator.paginate(any(), any())).thenReturn(expected);

        assertEquals(expected, service.findBookingsByUser(1L, State.CURRENT, -1, -1));
    }

    @Test
    public void givenUserId_whenFutureState_thenReturnList() {
        long bookingId = 1L;
        Booking booking = createBooking(bookingId, createItemWithId(1L, 2L), createUserWithId(3L));

        List<Booking> expected = List.of(booking);
        when(bookingRepo.findByBookerIdAndStartGreaterThanEqual(any(), any(), any()))
                .thenReturn(expected);
        when(paginator.paginate(any(), any())).thenReturn(expected);

        assertEquals(expected, service.findBookingsByUser(1L, State.FUTURE, -1, -1));
    }

    @Test
    public void givenUserId_whenPastState_thenReturnList() {
        long bookingId = 1L;
        Booking booking = createBooking(bookingId, createItemWithId(1L, 2L), createUserWithId(3L));

        List<Booking> expected = List.of(booking);
        when(bookingRepo.findByBookerIdAndEndLessThanEqual(any(), any(), any()))
                .thenReturn(expected);
        when(paginator.paginate(any(), any())).thenReturn(expected);

        assertEquals(expected, service.findBookingsByUser(1L, State.PAST, -1, -1));
    }

    @Test
    public void givenUserId_whenRejectedState_thenReturnList() {
        long bookingId = 1L;
        Booking booking = createBooking(bookingId, createItemWithId(1L, 2L), createUserWithId(3L));

        List<Booking> expected = List.of(booking);
        when(bookingRepo.findByBookerIdAndStatus(any(), any(), any()))
                .thenReturn(expected);
        when(paginator.paginate(any(), any())).thenReturn(expected);

        assertEquals(expected, service.findBookingsByUser(1L, State.REJECTED, -1, -1));
    }

    @Test
    public void givenUserId_whenWaitingState_thenReturnList() {
        long bookingId = 1L;
        Booking booking = createBooking(bookingId, createItemWithId(1L, 2L), createUserWithId(3L));

        List<Booking> expected = List.of(booking);
        when(bookingRepo.findByBookerIdAndStatus(any(), any(), any()))
                .thenReturn(expected);
        when(paginator.paginate(any(), any())).thenReturn(expected);

        assertEquals(expected, service.findBookingsByUser(1L, State.WAITING, -1, -1));
    }

    @Test
    public void givenUserId_whenAllState_thenReturnList() {
        long bookingId = 1L;
        Booking booking = createBooking(bookingId, createItemWithId(1L, 2L), createUserWithId(3L));

        List<Booking> expected = List.of(booking);
        when(bookingRepo.findByBookerIdOrderByStartDesc(any()))
                .thenReturn(expected);
        when(paginator.paginate(any(), any())).thenReturn(expected);

        assertEquals(expected, service.findBookingsByUser(1L, State.ALL, -1, -1));
    }

    @Test
    public void givenUserIdAndStateAndItemId_whenAny_thenReturnList() {
        service.findBookingsByUserForComment(1L, State.ALL, 1L);

        verify(bookingRepo, times(1))
                .findByItemIdAndBookerIdAndEndLessThanEqualAndStatus(eq(1L), eq(1L), any(), any(), eq(Status.APPROVED));
    }

    @Test
    public void givenOwnerId_whenNoObjectsFoundAndCurrentState_thenThrowNotFoundObjectException() {
        when(bookingRepo.findByItemOwnerIdAndStartLessThanEqualAndEndGreaterThanEqual(any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());
        assertThrows(NotFoundObjectException.class, () -> service.findBookingsByOwner(1L, State.CURRENT, -1, -1));
    }

    @Test
    public void givenOwnerId_whenCurrentState_thenReturnList() {
        long bookingId = 1L;
        Booking booking = createBooking(bookingId, createItemWithId(1L, 2L), createUserWithId(3L));

        List<Booking> expected = List.of(booking);
        when(bookingRepo.findByItemOwnerIdAndStartLessThanEqualAndEndGreaterThanEqual(any(), any(), any(), any()))
                .thenReturn(expected);
        when(paginator.paginate(any(), any())).thenReturn(expected);

        assertEquals(expected, service.findBookingsByOwner(1L, State.CURRENT, -1, -1));
    }

    @Test
    public void givenOwnerId_whenFutureState_thenReturnList() {
        long bookingId = 1L;
        Booking booking = createBooking(bookingId, createItemWithId(1L, 2L), createUserWithId(3L));

        List<Booking> expected = List.of(booking);
        when(bookingRepo.findByItemOwnerIdAndStartGreaterThanEqual(any(), any(), any()))
                .thenReturn(expected);
        when(paginator.paginate(any(), any())).thenReturn(expected);

        assertEquals(expected, service.findBookingsByOwner(1L, State.FUTURE, -1, -1));
    }

    @Test
    public void givenOwnerId_whenPastState_thenReturnList() {
        long bookingId = 1L;
        Booking booking = createBooking(bookingId, createItemWithId(1L, 2L), createUserWithId(3L));

        List<Booking> expected = List.of(booking);
        when(bookingRepo.findByItemOwnerIdAndEndLessThanEqual(any(), any(), any()))
                .thenReturn(expected);
        when(paginator.paginate(any(), any())).thenReturn(expected);

        assertEquals(expected, service.findBookingsByOwner(1L, State.PAST, -1, -1));
    }

    @Test
    public void givenOwnerId_whenRejectedState_thenReturnList() {
        long bookingId = 1L;
        Booking booking = createBooking(bookingId, createItemWithId(1L, 2L), createUserWithId(3L));

        List<Booking> expected = List.of(booking);
        when(bookingRepo.findByItemOwnerIdAndStatus(any(), any(), any()))
                .thenReturn(expected);
        when(paginator.paginate(any(), any())).thenReturn(expected);

        assertEquals(expected, service.findBookingsByOwner(1L, State.REJECTED, -1, -1));
    }

    @Test
    public void givenOwnerId_whenWaitingState_thenReturnList() {
        long bookingId = 1L;
        Booking booking = createBooking(bookingId, createItemWithId(1L, 2L), createUserWithId(3L));

        List<Booking> expected = List.of(booking);
        when(bookingRepo.findByItemOwnerIdAndStatus(any(), any(), any()))
                .thenReturn(expected);
        when(paginator.paginate(any(), any())).thenReturn(expected);

        assertEquals(expected, service.findBookingsByOwner(1L, State.WAITING, -1, -1));
    }

    @Test
    public void givenOwnerId_whenAllState_thenReturnList() {
        long bookingId = 1L;
        Booking booking = createBooking(bookingId, createItemWithId(1L, 2L), createUserWithId(3L));

        List<Booking> expected = List.of(booking);
        when(bookingRepo.findByItemOwnerIdOrderByStartDesc(any()))
                .thenReturn(expected);
        when(paginator.paginate(any(), any())).thenReturn(expected);

        assertEquals(expected, service.findBookingsByOwner(1L, State.ALL, -1, -1));
    }

    @Test
    public void givenItemId_whenFindingLastBooking_thenInvokeBookingRepo() {
        service.findLastBookingByItemId(1L);

        verify(bookingRepo, times(1))
                .findFirstByItemIdAndStartLessThanEqualAndStatus(any(), any(), any(), any());
    }

    @Test
    public void givenItemId_whenFindingNextBooking_thenInvokeBookingRepo() {
        service.findNextBookingByItemId(1L);

        verify(bookingRepo, times(1))
                .findFirstByItemIdAndStartAfterAndStatus(any(), any(), any(), any());
    }

    @Test
    public void givenItemIds_whenFindingLastBookings_thenInvokeBookingRepo() {
        service.findLastBookingByItemIds(Collections.singletonList(1L));

        verify(bookingRepo, times(1))
                .findByItemIdInAndStartLessThanEqualAndStatus(any(), any(), any(), any());
    }

    @Test
    public void givenItemIds_whenFindingNextBooking_thenInvokeBookingRepo() {
        service.findNextBookingByItemIds(Collections.singletonList(1L));

        verify(bookingRepo, times(1))
                .findByItemIdInAndStartAfterAndStatus(any(), any(), any(), any());
    }

    private Item createItemWithId(long id, long ownerId) {
        Item item = new Item();
        item.setId(id);
        item.setName("item" + id);
        item.setDescription("description" + id);
        item.setAvailable(true);
        item.setOwner(createUserWithId(ownerId));
        return item;
    }

    private User createUserWithId(long id) {
        User user = new User();
        user.setId(id);
        user.setName("user" + id);
        user.setEmail("user" + id + "@user.com");
        return user;
    }

    private Booking createBooking(long bookingId, Item item, User booker) {
        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setItem(item);
        booking.setBooker(booker);
        return booking;
    }

}