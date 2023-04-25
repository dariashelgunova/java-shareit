package ru.practicum.item.service;

import ru.practicum.item.comment.model.Comment;
import ru.practicum.item.model.Item;

import java.util.List;

public interface ItemService {
    List<Item> findAll();

    Item create(Item newItem, Long ownerId);

    Item update(Item newItem, Long ownerId, Long itemId);

    Item findById(Long itemId);

    void deleteById(Long itemId);

    void deleteAll();

    List<Item> findItemsByOwner(Long ownerId, Integer from, Integer size);

    List<Item> findItemsBySearch(String requestText, Integer from, Integer size);

    Comment addComment(Long itemId, Long userId, Comment newComment);

    Item addCommentsAndBookingsToItem(Item item, Long userId);

    List<Item> addCommentsAndBookingsToItems(List<Item> items, Long userId);

    List<Item> findItemsByRequestId(Long requestId);

    List<Item> findItemsByRequestIds(List<Long> requestIds);
}
