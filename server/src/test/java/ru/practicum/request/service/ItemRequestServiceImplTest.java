package ru.practicum.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.practicum.exception.NotFoundObjectException;
import ru.practicum.item.model.Item;
import ru.practicum.item.service.ItemService;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.request.repo.ItemRequestRepo;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    @InjectMocks
    ItemRequestServiceImpl service;
    @Mock
    ItemRequestRepo itemRequestRepo;
    @Mock
    UserService userService;
    @Mock
    ItemService itemService;


    @Test
    public void givenUserId_whenFindingAllWithWrongUserId_thenThrowNotFoundObjectException() {
        when(userService.findById(any())).thenThrow(NotFoundObjectException.class);

        assertThrows(NotFoundObjectException.class, () -> service.findAll(1L));
    }

    @Test
    public void givenUserId_whenFindingAllWithCorrectUserId_thenReturnItemRequestList() {
        User user = createUserWithId(1L);
        when(userService.findById(any())).thenReturn(user);
        ItemRequest itemRequest = createItemRequest(1L, user);
        List<ItemRequest> result = Collections.singletonList(itemRequest);
        when(itemRequestRepo.findByRequestorId(eq(1L), any())).thenReturn(result);

        assertEquals(result, service.findAll(1L));
    }

    @Test
    public void givenUserIdAndRequest_whenCreateRequestWithWrongUserId_thenThrowNotFoundObjectException() {
        User user = createUserWithId(1L);
        ItemRequest itemRequest = createItemRequest(1L, user);
        when(userService.findById(any())).thenThrow(NotFoundObjectException.class);

        assertThrows(NotFoundObjectException.class, () -> service.create(itemRequest, 1L));
    }

    @Test
    public void givenUserIdAndRequest_whenCreateRequestWithCorrectUserId_thenReturnRequest() {
        User user = createUserWithId(1L);
        ItemRequest itemRequest = createItemRequest(1L, user);
        when(userService.findById(any())).thenReturn(user);
        when(itemRequestRepo.save(itemRequest)).thenReturn(itemRequest);
        when(itemService.findItemsByRequestId(1L)).thenReturn(Collections.singletonList(createItemWithId(1L, 2L)));

        assertEquals(itemRequest, service.create(itemRequest, 1L));
    }

    @Test
    public void givenUserId_whenFindRequestsCreatedByUsers_thenInvokeItemRequestRepo() {
        service.findRequestsCreatedByUsers(0, 10, 1L);
        Sort sort = Sort.by(Sort.Direction.DESC, "created");

        verify(itemRequestRepo, times(1)).findByRequestorIdNot(any(), any());
    }

    @Test
    public void givenUserIdAndRequestId_whenFindRequestByIdWithWrongUserId_thenThrowNotFoundObjectException() {
        when(userService.findById(any())).thenThrow(NotFoundObjectException.class);

        assertThrows(NotFoundObjectException.class, () -> service.findById(1L, 1L));
    }

    @Test
    public void givenUserIdAndRequestId_whenFindRequestByIdWithNotFoundRequest_thenThrowNotFoundObjectException() {
        User user = createUserWithId(1L);
        when(userService.findById(any())).thenReturn(user);
        when(itemRequestRepo.findById(any())).thenThrow(NotFoundObjectException.class);

        assertThrows(NotFoundObjectException.class, () -> service.findById(1L, 1L));
    }

    @Test
    public void givenUserIdAndRequestId_whenFindRequestById_thenReturnRequest() {
        User user = createUserWithId(1L);
        ItemRequest itemRequest = createItemRequest(1L, user);
        when(userService.findById(any())).thenReturn(user);
        when(itemRequestRepo.findById(any())).thenReturn(Optional.of(itemRequest));

        assertEquals(itemRequest, service.findById(1L, 1L));
    }

    @Test
    public void givenRequestIdAndUserId_whenCheckingAndRequestIdNull_thenReturnNull() {
        assertNull(service.checkIfRequestIdIsNotNull(null, 1L));
    }

    @Test
    public void givenRequestIdAndUserId_whenCheckingAndRequestIdIsNotNull_thenReturnRequest() {
        User user = createUserWithId(1L);
        ItemRequest itemRequest = createItemRequest(1L, user);
        when(userService.findById(any())).thenReturn(user);
        when(itemRequestRepo.findById(any())).thenReturn(Optional.of(itemRequest));

        assertEquals(itemRequest, service.checkIfRequestIdIsNotNull(1L, 1L));
    }

    private ItemRequest createItemRequest(long requestId, User requestor) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(requestId);
        itemRequest.setDescription("description" + requestId);
        itemRequest.setRequestor(requestor);
        itemRequest.setItems(Collections.singletonList(createItemWithId(1L, 2L)));
        return itemRequest;
    }

    private User createUserWithId(long id) {
        User user = new User();
        user.setId(id);
        user.setName("user" + id);
        user.setEmail("user" + id + "@user.com");
        return user;
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

}