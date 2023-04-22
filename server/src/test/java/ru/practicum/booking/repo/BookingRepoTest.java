package ru.practicum.booking.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import ru.practicum.booking.model.Booking;
import ru.practicum.booking.model.Status;
import ru.practicum.item.model.Item;
import ru.practicum.item.repo.ItemRepo;
import ru.practicum.pageable.OffsetBasedPageRequest;
import ru.practicum.request.repo.ItemRequestRepo;
import ru.practicum.user.model.User;
import ru.practicum.user.repo.UserRepo;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BookingRepoTest {

    @Autowired
    private ItemRepo itemRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ItemRequestRepo itemRequestRepo;
    @Autowired
    private BookingRepo bookingRepo;
    private User owner;
    private User booker;
    private Booking createdBooking1;
    private Booking createdBooking2;
    private Item createdItem;
    private Sort sort = Sort.by(Sort.Direction.DESC, "start");
    private OffsetBasedPageRequest pageRequest = new OffsetBasedPageRequest(10, 0, sort);


    @BeforeEach
    public void init() {
        User itemOwner = createUser(2L);
        owner = userRepo.save(itemOwner);

        User bookingAuthor = createUser(3L);
        booker = userRepo.save(bookingAuthor);

        Item item = createItem(1L, owner);
        createdItem = itemRepo.save(item);

        Booking booking1 = createBooking1(item, booker);
        createdBooking1 = bookingRepo.save(booking1);

        Booking booking2 = createBooking2(item, booker);
        createdBooking2 = bookingRepo.save(booking2);
    }

    @Test
    public void givenBooking_whenFindingByBookerIdAndCurrentState_thenReturnBookingList() {
        LocalDateTime start = LocalDateTime.now().plus(1, ChronoUnit.HOURS);
        LocalDateTime end = LocalDateTime.now().plus(9, ChronoUnit.HOURS);
        assertEquals(1, bookingRepo.findByBookerIdAndStartLessThanEqualAndEndGreaterThanEqual(booker.getId(), start, end, pageRequest).size());
    }

    @Test
    public void givenBooking_whenFindingByBookerIdAndFutureState_thenReturnBookingList() {
        LocalDateTime start = LocalDateTime.now().minus(1, ChronoUnit.HOURS);
        assertEquals(2, bookingRepo.findByBookerIdAndStartGreaterThanEqual(booker.getId(), start, pageRequest).size());
    }

    @Test
    public void givenBooking_whenFindingByBookerIdAndPastState_thenReturnBookingList() {
        LocalDateTime end = LocalDateTime.now().plus(11, ChronoUnit.HOURS);
        assertEquals(1, bookingRepo.findByBookerIdAndEndLessThanEqual(booker.getId(), end, pageRequest).size());
    }

    @Test
    public void givenBooking_whenFindingByBookerIdAndStatus_thenReturnBookingList() {
        assertEquals(2, bookingRepo.findByBookerIdAndStatus(booker.getId(), Status.WAITING, pageRequest).size());
    }

    @Test
    public void givenBooking_whenFindingByBookerIdAndOrderByStartDesc_thenReturnBookingListInOrder() {
        assertEquals(createdBooking1, bookingRepo.findByBookerId(booker.getId(), pageRequest).get(1));
        assertEquals(createdBooking2, bookingRepo.findByBookerId(booker.getId(), pageRequest).get(0));
    }

    @Test
    public void givenBooking_whenFindingByOwnerIdAndCurrentState_thenReturnBookingList() {
        LocalDateTime start = LocalDateTime.now().plus(1, ChronoUnit.HOURS);
        LocalDateTime end = LocalDateTime.now().plus(9, ChronoUnit.HOURS);
        assertEquals(1, bookingRepo.findByItemOwnerIdAndStartLessThanEqualAndEndGreaterThanEqual(owner.getId(), start, end, pageRequest).size());
    }

    @Test
    public void givenBooking_whenFindingByOwnerIdAndFutureState_thenReturnBookingList() {
        LocalDateTime start = LocalDateTime.now().minus(1, ChronoUnit.HOURS);
        assertEquals(2, bookingRepo.findByItemOwnerIdAndStartGreaterThanEqual(owner.getId(), start, pageRequest).size());
    }

    @Test
    public void givenBooking_whenFindingByOwnerIdAndPastState_thenReturnBookingList() {
        LocalDateTime end = LocalDateTime.now().plus(11, ChronoUnit.HOURS);
        assertEquals(1, bookingRepo.findByItemOwnerIdAndEndLessThanEqual(owner.getId(), end, pageRequest).size());
    }

    @Test
    public void givenBooking_whenFindingByOwnerIdAndStatus_thenReturnBookingList() {
        assertEquals(2, bookingRepo.findByItemOwnerIdAndStatus(owner.getId(), Status.WAITING, pageRequest).size());
    }

    @Test
    public void givenBooking_whenFindingByOwnerIdAndOrderByStartDesc_thenReturnBookingListInOrder() {
        assertEquals(createdBooking1, bookingRepo.findByItemOwnerId(owner.getId(), pageRequest).get(1));
        assertEquals(createdBooking2, bookingRepo.findByItemOwnerId(owner.getId(), pageRequest).get(0));
    }

    @Test
    public void givenBooking_whenFindingLastBooking_thenReturnBookingList() {
        createdBooking1.setStatus(Status.APPROVED);
        bookingRepo.save(createdBooking1);

        LocalDateTime start = LocalDateTime.now().plus(1, ChronoUnit.HOURS);
        assertEquals(createdBooking1, bookingRepo.findFirstByItemIdAndStartLessThanEqualAndStatus(createdItem.getId(), start, Status.APPROVED, sort));
    }

    @Test
    public void givenBooking_whenFindingNextBooking_thenReturnBookingList() {
        createdBooking2.setStatus(Status.APPROVED);
        bookingRepo.save(createdBooking2);

        LocalDateTime start = LocalDateTime.now().plus(1, ChronoUnit.HOURS);
        assertEquals(createdBooking2, bookingRepo.findFirstByItemIdAndStartAfterAndStatus(createdItem.getId(), start, Status.APPROVED, sort));
    }

    @Test
    public void givenBooking_whenFindingLastBookings_thenReturnBookingList() {
        createdBooking1.setStatus(Status.APPROVED);
        bookingRepo.save(createdBooking1);

        LocalDateTime start = LocalDateTime.now().plus(1, ChronoUnit.HOURS);
        assertEquals(1, bookingRepo.findByItemIdInAndStartLessThanEqualAndStatus(Collections.singletonList(createdItem.getId()), start, Status.APPROVED, sort).size());
        assertEquals(createdBooking1, bookingRepo.findByItemIdInAndStartLessThanEqualAndStatus(Collections.singletonList(createdItem.getId()), start, Status.APPROVED, sort).get(0));
    }

    @Test
    public void givenBooking_whenFindingNextBookings_thenReturnBookingList() {
        createdBooking2.setStatus(Status.APPROVED);
        bookingRepo.save(createdBooking2);

        LocalDateTime start = LocalDateTime.now().plus(1, ChronoUnit.HOURS);
        assertEquals(createdBooking2, bookingRepo.findByItemIdInAndStartAfterAndStatus(Collections.singletonList(createdItem.getId()), start, Status.APPROVED, sort).get(0));
        assertEquals(1, bookingRepo.findByItemIdInAndStartAfterAndStatus(Collections.singletonList(createdItem.getId()), start, Status.APPROVED, sort).size());
    }

    @Test
    public void givenBooking_whenFindingPastBookingsForComment_thenReturnBookingList() {
        createdBooking1.setStatus(Status.APPROVED);
        bookingRepo.save(createdBooking1);

        LocalDateTime end = LocalDateTime.now().plus(11, ChronoUnit.HOURS);
        assertEquals(1, bookingRepo.findByItemIdAndBookerIdAndEndLessThanEqualAndStatus(createdItem.getId(), booker.getId(), end, sort, Status.APPROVED).size());
    }

    private User createUser(Long id) {
        User user = new User();
        user.setName("name" + id);
        user.setEmail("yandex" + id + "@yandex.ru");
        return user;
    }

    private Item createItem(Long id, User owner) {
        Item item = new Item();
        item.setAvailable(true);
        item.setName("name" + id);
        item.setDescription("description" + id);
        item.setOwner(owner);

        return item;
    }

    private Booking createBooking1(Item item, User booker) {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plus(10, ChronoUnit.HOURS);
        return new Booking(null, start, end, item, booker, Status.WAITING);
    }

    private Booking createBooking2(Item item, User booker) {
        LocalDateTime start = LocalDateTime.now().plus(1, ChronoUnit.DAYS);
        LocalDateTime end = start.plus(10, ChronoUnit.HOURS);
        return new Booking(null, start, end, item, booker, Status.WAITING);
    }
}