package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    List<Item> findAll();

    Item create(Item newItem, Long ownerId);

    Item update(Item newItem, Long ownerId, Long itemId);

    Item findById(Long itemId);

    void deleteById(Long itemId);

    void deleteAll();

    List<Item> findItemsByOwner(Long ownerId);

    List<Item> findItemsBySearch(String requestText);

    Comment addComment(Long itemId, Long userId, Comment newComment);
}
