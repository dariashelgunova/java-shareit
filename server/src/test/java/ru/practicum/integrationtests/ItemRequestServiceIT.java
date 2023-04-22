package ru.practicum.integrationtests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.context.WebApplicationContext;
import ru.practicum.item.model.Item;
import ru.practicum.item.service.ItemService;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.request.repo.ItemRequestRepo;
import ru.practicum.request.service.ItemRequestService;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class ItemRequestServiceIT {
    private static User owner;
    private static Item createdItem1;
    private static User requestAuthor;
    private static ItemRequest createdRequest;
    @Autowired
    ItemRequestService itemRequestService;
    @Autowired
    ItemRequestRepo itemRequestRepo;
    @Autowired
    private WebApplicationContext applicationContext;
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;

    @BeforeEach
    void setup() {
        User user1 = createUser(1L);
        owner = userService.create(user1);

        User user2 = createUser(2L);
        requestAuthor = userService.create(user2);
        Item item = createItem(1L, owner);
        createdItem1 = itemService.create(item, owner.getId());

        ItemRequest itemRequest = createItemRequest(1L, requestAuthor, createdItem1);
        createdRequest = itemRequestRepo.save(itemRequest);
    }

    @Test
    public void given1ExistingRequest_whenFindingAll_thenReturn1Request() {
        List<ItemRequest> requests = itemRequestService.findAll(requestAuthor.getId());

        assertEquals(1, requests.size());
    }

    @Test
    public void givenNewItemRequest_whenCreatingRequest_thenReturnCreatedRequest() {
        Long createdRequestId = itemRequestService.create(createdRequest, requestAuthor.getId()).getId();
        assertTrue(itemRequestRepo.findById(createdRequestId).isPresent());
    }

    @Test
    public void givenExistingRequest_whenFindingRequestsCreatedByOthersByOwner_thenReturnRequest() {
        Long ownerId = owner.getId();

        assertEquals(createdRequest, itemRequestService.findRequestsCreatedByUsers(0, 10, ownerId).get(0));
    }

    @Test
    public void givenExistingRequest_whenFindingById_thenReturnRequest() {
        ItemRequest foundItemRequest = itemRequestService.findById(createdRequest.getId(), owner.getId());

        assertEquals(createdRequest, foundItemRequest);
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

    private ItemRequest createItemRequest(Long id, User requestor, Item item) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("description");
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestor(requestor);
        itemRequest.setItems(Collections.singletonList(item));

        return itemRequest;
    }
}
