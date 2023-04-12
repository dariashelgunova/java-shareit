package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.AvailabilityException;
import ru.practicum.shareit.exception.NotFoundObjectException;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repo.CommentRepo;
import ru.practicum.shareit.item.comment.service.CommentService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.db.ItemRepo;
import ru.practicum.shareit.paginator.Paginator;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ItemServiceImplTest {

    @InjectMocks
    ItemServiceImpl service;
    @Mock
    ItemRepo itemRepo;
    @Mock
    UserService userService;
    @Mock
    BookingService bookingService;
    @Mock
    CommentRepo commentRepo;
    @Mock
    Paginator<Item> itemPaginator;
    @Mock
    CommentService commentService;

    @BeforeEach
    public void before() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenItems_whenFindingAllItems_thenInvokeItemRepo() {
        when(itemRepo.findAll()).thenReturn(Collections.emptyList());

        service.findAll();

        verify(itemRepo, times(1)).findAll();
    }

    @Test
    public void givenNewItem_whenItemOwnerIsNotFound_thenThrowNotFoundObjectException() {
        long ownerId = 1L;
        Item newItem = createItemWithId(10L);
        when(userService.findById(ownerId)).thenThrow(NotFoundObjectException.class);

        assertThrows(NotFoundObjectException.class, () -> service.create(newItem, ownerId));
    }

    @Test
    public void givenNewItem_whenCreatingItem_thenReturnItemWithOwner() {
        long ownerId = 1L;
        Item newItem = createItemWithId(10L);
        User owner = createUserWithId(ownerId);
        when(userService.findById(ownerId)).thenReturn(owner);
        when(itemRepo.save(newItem)).thenReturn(newItem);

        Item createdItem = service.create(newItem, ownerId);

        assertEquals(ownerId, createdItem.getOwner().getId());
    }

    @Test
    public void givenUpdatedItem_whenItemNotFound_throwNotFoundObjectException() {
        long itemId = 1L;
        Item item = createItemWithId(itemId);
        when(itemRepo.findById(itemId)).thenThrow(NotFoundObjectException.class);

        assertThrows(NotFoundObjectException.class, () -> service.update(item, 1L, itemId));
    }

    @Test
    public void givenUpdatedItem_whenItemIsUpdatedNotByOwner_throwAccessDeniedException() {
        long wrongOwnerId = 3L;
        long ownerId = 2L;
        long itemId = 1L;
        Item updatedItem = createItemWithId(itemId);
        when(itemRepo.findById(itemId)).thenReturn(Optional.of(createItemWithOwner(itemId, ownerId)));

        assertThrows(AccessDeniedException.class, () -> service.update(updatedItem, wrongOwnerId, itemId));
    }

    @Test
    public void givenUpdatedItem_whenItemIsUpdatedByOwner_returnUpdatedItem() {
        long ownerId = 2L;
        long itemId = 1L;
        Item updatedItem = createItemWithId(itemId);
        when(itemRepo.findById(itemId)).thenReturn(Optional.of(createItemWithOwner(itemId, ownerId)));

        Item result = service.update(updatedItem, ownerId, itemId);

        assertNotNull(result);
    }

    @Test
    public void givenUpdatedItemWithName_whenUpdatingItem_returnItemWithUpdatedName() {
        String itemName = "ItemName";
        long ownerId = 2L;
        long itemId = 1L;
        Item updatedItem = createItemWithId(itemId);
        updatedItem.setName(itemName);
        when(itemRepo.findById(itemId)).thenReturn(Optional.of(createItemWithOwner(itemId, ownerId)));

        Item result = service.update(updatedItem, ownerId, itemId);

        assertEquals(itemName, result.getName());
    }

    @Test
    public void givenUpdatedItemWithDescription_whenUpdatingItem_returnItemWithUpdatedDescription() {
        String itemDescription = "ItemDescription";
        long ownerId = 2L;
        long itemId = 1L;
        Item updatedItem = createItemWithId(itemId);
        updatedItem.setDescription(itemDescription);
        when(itemRepo.findById(itemId)).thenReturn(Optional.of(createItemWithOwner(itemId, ownerId)));

        Item result = service.update(updatedItem, ownerId, itemId);

        assertEquals(itemDescription, result.getDescription());
    }

    @Test
    public void givenUpdatedItemWithAvailableNotNull_whenUpdatingItem_returnItemWithUpdatedAvailable() {
        Boolean available = true;
        long ownerId = 2L;
        long itemId = 1L;
        Item updatedItem = createItemWithId(itemId);
        updatedItem.setAvailable(available);
        when(itemRepo.findById(itemId)).thenReturn(Optional.of(createItemWithOwner(itemId, ownerId)));

        Item result = service.update(updatedItem, ownerId, itemId);

        assertEquals(available, result.getAvailable());
    }

    @Test
    public void givenItemId_whenNoItemWithIdPresent_throwNotFoundObjectException() {
        long itemId = 1L;
        when(itemRepo.findById(itemId)).thenThrow(NotFoundObjectException.class);

        assertThrows(NotFoundObjectException.class, () -> service.findById(itemId));
    }

    @Test
    public void givenItemId_whenItemWithIdPresent_returnItemWithGivenId() {
        long itemId = 1L;
        when(itemRepo.findById(itemId)).thenReturn(Optional.of(createItemWithId(itemId)));

        Item foundItem = service.findById(itemId);

        assertEquals(itemId, foundItem.getId());
    }

    @Test
    public void givenItemId_whenDeletingItemById_thenInvokeItemRepo() {
        long itemId = 1L;
        doNothing().when(itemRepo).deleteById(itemId);

        service.deleteById(itemId);

        verify(itemRepo, times(1)).deleteById(itemId);
    }

    @Test
    public void givenItemId_whenDeletingAllItems_thenInvokeItemRepo() {
        doNothing().when(itemRepo).deleteAll();

        service.deleteAll();

        verify(itemRepo, times(1)).deleteAll();
    }

    @Test
    public void givenOwnerId_whenNoItemsFound_thenThrowNotFoundObjectException() {
        long ownerId = 1L;
        when(itemRepo.findByOwnerId(ownerId)).thenReturn(Collections.emptyList());

        assertThrows(NotFoundObjectException.class, () -> service.findItemsByOwner(ownerId, -1, -1));
    }

    @Test
    public void givenOwnerId_whenItemsFound_thenReturnFoundItems() {
        long itemId = 1L;
        long ownerId = 1L;
        List<Item> expected = List.of(createItemWithOwner(itemId, ownerId));
        when(itemRepo.findByOwnerId(ownerId)).thenReturn(expected);
        when(itemPaginator.paginate(any(), any())).thenReturn(expected);

        List<Item> itemsByOwner = service.findItemsByOwner(ownerId, -1, -1);

        assertEquals(1, itemsByOwner.size());
    }

    @Test
    public void givenBlankRequestText_whenFindingItemsByEmptyText_thenReturnEmptyList() {
        String emptyRequestText = "";

        List<Item> foundItems = service.findItemsBySearch(emptyRequestText, -1, -1);

        assertTrue(foundItems.isEmpty());
    }

    @Test
    public void givenRequestText_whenNoItemsFoundBySearch_thenThrowNotFoundObjectException() {
        String requestText = "text";
        when(itemRepo.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(requestText, requestText, true))
                .thenReturn(Collections.emptyList());

        assertThrows(NotFoundObjectException.class, () -> service.findItemsBySearch(requestText, -1, -1));
    }

    @Test
    public void givenRequestText_whenItemsFoundBySearch_thenReturnFoundItems() {
        String requestText = "text";
        List<Item> expected = List.of(createItemWithOwner(1L, 1L));
        when(itemRepo.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(requestText, requestText, true))
                .thenReturn(expected);
        when(itemPaginator.paginate(any(), any())).thenReturn(expected);

        List<Item> foundItems = service.findItemsBySearch(requestText, -1, -1);

        assertEquals(1, foundItems.size());
    }

    @Test
    public void givenUserIdAndItemId_whenNoBookingsFound_thenThrowAvailabilityException() {
        long userId = 1L;
        long itemId = 1L;
        when(bookingService.findBookingsByUserForComment(eq(userId), any(), eq(itemId)))
                .thenReturn(Collections.emptyList());

        assertThrows(AvailabilityException.class, () -> service.addComment(itemId, userId, null));
    }

    @Test
    public void givenUserIdAndItemIdAndComment_whenBookingsFound_thenReturnSavedComment() {
        long userId = 1L;
        long itemId = 1L;
        Item item = createItemWithId(itemId);
        User user = createUserWithId(userId);
        Comment comment = createComment(item, user);
        when(bookingService.findBookingsByUserForComment(eq(userId), any(), eq(itemId)))
                .thenReturn(List.of(createBooking(itemId, item, user)));
        when(commentRepo.save(comment)).thenReturn(comment);

        Comment savedComment = service.addComment(itemId, userId, comment);

        assertNotNull(savedComment);
    }

    @Test
    public void givenItem_whenCommentsAreFoundByItem_thenSetItemComments() {
        long itemId = 1L;
        Item item = createItemWithId(itemId);
        when(commentService.findByItemId(itemId)).thenReturn(List.of(createComment(item, null)));
        when(bookingService.findLastBookingByItemId(any())).thenReturn(null);
        when(bookingService.findNextBookingByItemId(any())).thenReturn(null);
        when(bookingService.checkAccessForItemLastNextBookingByUserId(any(), any())).thenReturn(true);

        Item itemWithComment = service.addCommentsAndBookingsToItem(item, null);

        assertNotNull(itemWithComment.getComments());
    }

    @Test
    public void givenItemAndUserId_whenLastBookingIsFoundButNoAccessPresent_thenDoNotSetLastBooking() {
        long itemId = 1L;
        long userId = 1L;
        Item item = createItemWithId(itemId);
        User user = createUserWithId(userId);
        Booking lastBooking = createBooking(1L, item, user);
        when(commentService.findByItemId(itemId)).thenReturn(null);
        when(bookingService.findLastBookingByItemId(any())).thenReturn(lastBooking);
        when(bookingService.checkAccessForItemLastNextBookingByUserId(lastBooking, userId)).thenReturn(false);
        when(bookingService.findNextBookingByItemId(any())).thenReturn(null);

        Item itemWithComment = service.addCommentsAndBookingsToItem(item, userId);

        assertNull(itemWithComment.getLastBooking());
    }

    @Test
    public void givenItemAndUserId_whenLastBookingIsFoundButAccessPresent_thenSetLastBooking() {
        long itemId = 1L;
        long userId = 1L;
        Item item = createItemWithId(itemId);
        User user = createUserWithId(userId);
        Booking lastBooking = createBooking(1L, item, user);
        when(commentService.findByItemId(itemId)).thenReturn(null);
        when(bookingService.findLastBookingByItemId(any())).thenReturn(lastBooking);
        when(bookingService.checkAccessForItemLastNextBookingByUserId(lastBooking, userId)).thenReturn(true);
        when(bookingService.findNextBookingByItemId(any())).thenReturn(null);

        Item itemWithComment = service.addCommentsAndBookingsToItem(item, userId);

        assertNotNull(itemWithComment.getLastBooking());
    }

    @Test
    public void givenItemAndUserId_whenNextBookingIsFoundButNoAccessPresent_thenDoNotSetNextBooking() {
        long itemId = 1L;
        long userId = 1L;
        Item item = createItemWithId(itemId);
        User user = createUserWithId(userId);
        Booking nextBooking = createBooking(1L, item, user);
        when(commentService.findByItemId(itemId)).thenReturn(null);
        when(bookingService.findLastBookingByItemId(any())).thenReturn(null);
        when(bookingService.checkAccessForItemLastNextBookingByUserId(null, userId)).thenReturn(false);
        when(bookingService.findNextBookingByItemId(any())).thenReturn(nextBooking);
        when(bookingService.checkAccessForItemLastNextBookingByUserId(nextBooking, userId)).thenReturn(false);

        Item itemWithComment = service.addCommentsAndBookingsToItem(item, userId);

        assertNull(itemWithComment.getNextBooking());
    }

    @Test
    public void givenItemAndUserId_whenNextBookingIsFoundButAccessPresent_thenSetNextBooking() {
        long itemId = 1L;
        long userId = 1L;
        Item item = createItemWithId(itemId);
        User user = createUserWithId(userId);
        Booking nextBooking = createBooking(1L, item, user);
        when(commentService.findByItemId(itemId)).thenReturn(null);
        when(bookingService.findLastBookingByItemId(any())).thenReturn(null);
        when(bookingService.checkAccessForItemLastNextBookingByUserId(null, userId)).thenReturn(false);
        when(bookingService.findNextBookingByItemId(any())).thenReturn(nextBooking);
        when(bookingService.checkAccessForItemLastNextBookingByUserId(nextBooking, userId)).thenReturn(true);

        Item itemWithComment = service.addCommentsAndBookingsToItem(item, userId);

        assertNotNull(itemWithComment.getNextBooking());
    }

    @Test
    public void givenNoItemsAndUserId_whenAddCommentsAndBookingsToItems_thenReturnEmptyList() {
        List<Item> itemsByOwner = Collections.emptyList();

        List<Item> result = service.addCommentsAndBookingsToItems(itemsByOwner, 1L);

        assertTrue(result.isEmpty());
    }

    @Test
    public void givenItemsAndUserId_whenCommentByItemFound_thenReturnItemWithComment() {
        long itemId = 1L;
        long ownerId = 1L;
        Item item = createItemWithOwner(itemId, ownerId);
        User user = createUserWithId(1L);
        Comment comment = createComment(item, user);
        when(commentService.findByItemIn(any())).thenReturn(Collections.singletonList(comment));
        List<Item> itemsByOwner = Collections.singletonList(item);

        List<Item> result = service.addCommentsAndBookingsToItems(itemsByOwner, ownerId);

        assertNotNull(result.get(0).getComments());
    }

    @Test
    public void givenItemsAndUserId_whenLastBookingByItemFound_thenReturnItemWithLastBooking() {
        long itemId = 1L;
        long ownerId = 1L;
        Item item = createItemWithOwner(itemId, ownerId);
        User user = createUserWithId(1L);
        Booking booking = createBooking(1L, item, user);
        when(bookingService.findLastBookingByItemIds(any())).thenReturn(Collections.singletonList(booking));
        List<Item> itemsByOwner = Collections.singletonList(item);

        List<Item> result = service.addCommentsAndBookingsToItems(itemsByOwner, ownerId);

        assertNotNull(result.get(0).getLastBooking());
    }

    @Test
    public void givenItemsAndUserId_whenNextBookingByItemFound_thenReturnItemWithNextBooking() {
        long itemId = 1L;
        long ownerId = 1L;
        Item item = createItemWithOwner(itemId, ownerId);
        User user = createUserWithId(1L);
        Booking booking = createBooking(1L, item, user);
        when(bookingService.findNextBookingByItemIds(any())).thenReturn(Collections.singletonList(booking));
        List<Item> itemsByOwner = Collections.singletonList(item);

        List<Item> result = service.addCommentsAndBookingsToItems(itemsByOwner, ownerId);

        assertNotNull(result.get(0).getNextBooking());
    }

    private Item createItemWithOwner(long itemId, long ownerId) {
        Item item = createItemWithId(itemId);
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

    private Item createItemWithId(long id) {
        Item item = new Item();
        item.setId(id);
        item.setName("item" + id);
        item.setDescription("description" + id);
        item.setAvailable(true);
        return item;
    }

    private Comment createComment(Item item, User user) {
        Comment comment = new Comment();
        comment.setId((long) 1);
        comment.setText("Хорошо");
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        return comment;
    }

    private Booking createBooking(long bookingId, Item item, User booker) {
        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setItem(item);
        booking.setBooker(booker);
        return booking;
    }

}