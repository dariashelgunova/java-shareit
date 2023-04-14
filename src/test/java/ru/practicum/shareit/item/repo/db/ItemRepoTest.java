package ru.practicum.shareit.item.repo.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.ItemRepo;
import ru.practicum.shareit.pageable.OffsetBasedPageRequest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repo.ItemRequestRepo;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repo.UserRepo;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRepoTest {

    @Autowired
    private ItemRepo itemRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ItemRequestRepo itemRequestRepo;
    private User owner;
    private User author;
    private ItemRequest createdRequest;
    private Item createdItem;


    @BeforeEach
    public void init() {
        User itemOwner = createUser(2L);
        owner = userRepo.save(itemOwner);

        User requestAuthor = createUser(3L);
        author = userRepo.save(requestAuthor);

        ItemRequest itemRequest = createItemRequest(author);
        createdRequest = itemRequestRepo.save(itemRequest);

        Item item = createItem(1L, owner, createdRequest);
        createdItem = itemRepo.save(item);
    }

    @Test
    public void givenItem_whenFindingByOwnerId_thenReturnItem() {
        assertEquals(1, itemRepo.findByOwnerId(owner.getId(), new OffsetBasedPageRequest(10, 0, null)).size());
    }

    @Test
    public void givenItem_whenFindingBySearchDescription_thenReturnItem() {
        assertEquals(1, itemRepo.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable("description", "description", true, new OffsetBasedPageRequest(10, 0, null)).size());
    }

    @Test
    public void givenItem_whenFindingBySearchName_thenReturnItem() {
        assertEquals(1, itemRepo.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable("Name", "Name", true, new OffsetBasedPageRequest(10, 0, null)).size());
    }

    @Test
    public void givenItem_whenFindingByRequestId_thenReturnItem() {
        assertEquals(1, itemRepo.findByRequestId(createdRequest.getId()).size());
    }

    @Test
    public void givenItem_whenFindingByRequestIdIn_thenReturnItemList() {
        assertEquals(1, itemRepo.findByRequestIdIn(Collections.singletonList(createdRequest.getId())).size());
    }

    private User createUser(Long id) {
        User user = new User();
        user.setName("name" + id);
        user.setEmail("yandex" + id + "@yandex.ru");
        return user;
    }

    private Item createItem(Long id, User owner, ItemRequest request) {
        Item item = new Item();
        item.setAvailable(true);
        item.setName("name" + id);
        item.setDescription("description" + id);
        item.setOwner(owner);
        item.setRequest(request);

        return item;
    }

    private ItemRequest createItemRequest(User requestor) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("description");
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestor(requestor);

        return itemRequest;
    }
}