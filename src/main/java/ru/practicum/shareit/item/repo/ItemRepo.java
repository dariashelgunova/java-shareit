package ru.practicum.shareit.item.repo;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepo {
    List<Item> findAll();

    Item create(Item item);

    Item update(Item item);

    Optional<Item> findById(Long itemId);

    void deleteById(Long itemId);

    void deleteAll();
}
