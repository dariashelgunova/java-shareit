package ru.practicum.shareit.request.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundObjectException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.paginator.Page;
import ru.practicum.shareit.paginator.Paginator;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repo.ItemRequestRepo;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    ItemRequestRepo itemRequestRepo;

    UserService userService;

    ItemService itemService;

    Paginator<ItemRequest> paginator;

    public List<ItemRequest> findAll(Long userId) {
        userService.findById(userId);
        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        List<ItemRequest> result = itemRequestRepo.findByRequestorId(userId, sort);
        return addItemsToItemRequests(result);
    }

    @Transactional
    public ItemRequest create(ItemRequest itemRequest, Long userId) {
        User requestor = userService.findById(userId);
        itemRequest.setRequestor(requestor);
        ItemRequest result = itemRequestRepo.save(itemRequest);
        return addItemsToItemRequest(result);
    }

    public List<ItemRequest> findRequestsCreatedByUsers(Integer from, Integer size, Long userId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        List<ItemRequest> all = itemRequestRepo.findByRequestorIdNot(userId, sort);
        List<ItemRequest> result = paginator.paginate(new Page(from, size), all);
        return addItemsToItemRequests(result);
    }

    public ItemRequest findById(Long requestId, Long userId) {
        userService.findById(userId);
        ItemRequest result = getItemRequestByIdOrThrowException(requestId);
        return addItemsToItemRequest(result);
    }

    private ItemRequest getItemRequestByIdOrThrowException(Long requestId) {
        return itemRequestRepo.findById(requestId)
                .orElseThrow(() -> new NotFoundObjectException("Объект не был найден"));
    }

    private ItemRequest addItemsToItemRequest(ItemRequest itemRequest) {
        List<Item> items = itemService.findItemsByRequestId(itemRequest.getId());
        itemRequest.setItems(items);
        return itemRequest;
    }

    private List<ItemRequest> addItemsToItemRequests(List<ItemRequest> itemRequests) {
        List<Long> itemRequestsIds = itemRequests
                .stream()
                .map(ItemRequest::getId)
                .collect(toList());

        Map<ItemRequest, List<Item>> items = itemService.findItemsByRequestIds(itemRequestsIds)
                .stream()
                .collect(groupingBy(Item::getRequest, toList()));

        for (ItemRequest itemRequest : itemRequests) {
            itemRequest.setItems(items.getOrDefault(itemRequest, Collections.emptyList()));
        }
        return itemRequests;
    }

    public ItemRequest checkIfRequestIdIsNotNull(Long requestId, Long userId) {
        if (requestId != null) {
            return findById(requestId, userId);
        } else {
            return null;
        }
    }
}