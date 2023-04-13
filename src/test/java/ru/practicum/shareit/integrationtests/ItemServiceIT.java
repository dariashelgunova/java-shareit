package ru.practicum.shareit.integrationtests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.context.WebApplicationContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repo.BookingRepo;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.ItemRepo;
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
public class ItemServiceIT {
    @Autowired
    private WebApplicationContext applicationContext;
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private ItemRepo itemRepo;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private BookingRepo bookingRepo;

    private User owner;
    private Item createdItem1;
    private Comment comment;
    private User author;
    private Booking createdBooking1;

    @BeforeEach
    void setup() {
        User user1 = createUser(1L);
        owner = userService.create(user1);

        User user2 = createUser(2L);
        author = userService.create(user2);
        Item item = createItem(1L, owner);
        createdItem1 = itemRepo.save(item);

        comment = createComment();
    }

    @Test
    public void given1ExistingItem_whenFindingAll_thenReturn1Item() {
        List<Item> items = itemService.findAll();

        assertEquals(1, items.size());
    }

    @Test
    public void givenNewItem_whenCreatingItem_thenReturnCreatedItem() {
        Long createdItemId = itemService.create(createdItem1, owner.getId()).getId();
        assertTrue(itemRepo.findById(createdItemId).isPresent());
    }

    @Test
    public void givenExistingItem_whenUpdatingItem_thenItemIsUpdatedSuccessfully() {
        Long existingItemId = createdItem1.getId();
        String newDescription = "newDescription";

        createdItem1.setDescription(newDescription);

        itemService.update(createdItem1, createdItem1.getOwner().getId(), existingItemId);

        Item updatedItem = itemRepo.findById(existingItemId).orElseThrow();
        assertEquals(newDescription, updatedItem.getDescription());
    }

    @Test
    public void givenExistingItem_whenFindingById_thenReturnItem() {
        Item foundItem = itemService.findById(createdItem1.getId());

        assertEquals(createdItem1, foundItem);
    }

    @Test
    public void givenExistingItem_whenDeleteById_thenRepoIsEmpty() {
        itemService.deleteById(createdItem1.getId());

        assertEquals(0, itemService.findAll().size());
    }

    @Test
    public void givenExistingItem_whenDeleteAll_thenRepoIsEmpty() {
        itemService.deleteAll();

        assertEquals(0, itemService.findAll().size());
    }

    @Test
    public void givenExistingItem_whenFindingByOwner_thenReturnItem() {
        Long ownerId = owner.getId();

        assertEquals(createdItem1, itemService.findItemsByOwner(ownerId, 0, 10).get(0));
    }

    @Test
    public void givenExistingItem_whenFindingBySearch_thenReturnItem() {
        String searchText = "description";

        assertEquals(createdItem1, itemService.findItemsBySearch(searchText, 0, 10).get(0));
    }

    @Test
    public void givenItemIdAndUserIdAndNewCooment_whenAddNewComment_thenReturnComment() {
        Booking booking1 = createBooking1(createdItem1, author);
        createdBooking1 = bookingService.create(booking1, author.getId());
        createdBooking1.setStatus(Status.APPROVED);
        bookingService.acceptOrRejectRequest(true, owner.getId(), booking1.getId());

        Long authorId = author.getId();
        Long itemId = createdItem1.getId();
        Comment newComment = itemService.addComment(itemId, authorId, comment);

        assertEquals("text", newComment.getText());
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

    private Comment createComment() {
        Comment comment = new Comment();
        comment.setCreated(LocalDateTime.now());
        comment.setText("text");
        return comment;
    }

    private Booking createBooking1(Item item, User booker) {
        LocalDateTime start = LocalDateTime.now().minus(5, ChronoUnit.HOURS);
        LocalDateTime end = start.plus(10, ChronoUnit.SECONDS);
        return new Booking(null, start, end, item, booker, Status.APPROVED);
    }
}
