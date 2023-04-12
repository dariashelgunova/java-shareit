package ru.practicum.shareit.integrationtests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.context.WebApplicationContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repo.BookingRepo;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class BookingServiceIT {
    private static Booking createdBooking1;
    private static Booking createdBooking2;
    private static User owner;
    private static User booker;
    private static Item createdItem;
    @Autowired
    ItemService itemService;
    @Autowired
    UserService userService;
    @Autowired
    private WebApplicationContext applicationContext;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private BookingRepo bookingRepo;

    @BeforeEach
    void setup() {
        User user1 = createUser(1L);
        owner = userService.create(user1);
        Item item = createItem(1L, owner);
        createdItem = itemService.create(item, owner.getId());

        User user2 = createUser(2L);
        booker = userService.create(user2);

        Booking booking1 = createBooking1(createdItem, booker);
        createdBooking1 = bookingRepo.save(booking1);
        Booking booking2 = createBooking2(createdItem, booker);
        createdBooking2 = bookingRepo.save(booking2);
    }

    @Test
    public void givenNewBooking_whenCreatingBooking_thenReturnCreatedBooking() {
        Long createdBooking1Id = createdBooking1.getId();
        assertTrue(bookingRepo.findById(createdBooking1Id).isPresent());
    }

    @Test
    public void givenExistingBooking_whenAcceptingIt_thenReturnAcceptedBookingWithNewStatus() {
        Long ownerId = owner.getId();
        Long booking1Id = createdBooking1.getId();
        Booking returnedBooking = bookingService.acceptOrRejectRequest(true, ownerId, booking1Id);

        assertEquals(Status.APPROVED, returnedBooking.getStatus());
    }

    @Test
    public void givenExistingBooking_whenFindingById_thenReturnBooking() {
        Booking foundBooking = bookingService.findById(createdBooking1.getId());

        assertEquals(createdBooking1, foundBooking);
    }

    @Test
    public void givenUserId_whenFindingBookingsByUser_thenReturn2Bookings() {
        Long bookerId = booker.getId();
        List<Booking> result = bookingService.findBookingsByUser(bookerId, State.ALL, -1, -1);
        List<Booking> expected = List.of(createdBooking1, createdBooking2);

        assertEquals(expected.size(), result.size());
    }

    @Test
    public void givenUserId_whenFindingBookingsByOwner_thenReturn2Bookings() {
        Long ownerId = owner.getId();
        List<Booking> result = bookingService.findBookingsByOwner(ownerId, State.ALL, -1, -1);
        List<Booking> expected = List.of(createdBooking1, createdBooking2);

        assertEquals(expected.size(), result.size());
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
        item.setOwner(owner);
        item.setName("item" + id);
        item.setDescription("description" + id);
        return item;
    }

    private Booking createBooking1(Item item, User booker) {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plus(10, ChronoUnit.SECONDS);
        return new Booking(null, start, end, item, booker, Status.WAITING);
    }

    private Booking createBooking2(Item item, User booker) {
        LocalDateTime start = LocalDateTime.now().plus(5, ChronoUnit.HOURS);
        LocalDateTime end = start.plus(10, ChronoUnit.SECONDS);
        return new Booking(null, start, end, item, booker, Status.WAITING);
    }
}
