package ru.practicum.shareit.item.repo;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundObjectException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRepoInMemory implements ItemRepo {
    final Map<Long, Item> repo = new HashMap<>();
    final Map<Long, List<Item>> userItemIndex = new LinkedHashMap<>();
    Long idCounter = 0L;

    public List<Item> findAll() {
        return new ArrayList<Item>(repo.values());
    }

    public Item create(Item item) {
        item.setId(++idCounter);
        repo.put(idCounter, item);
        final List<Item> items = userItemIndex.computeIfAbsent(item.getOwner().getId(), k -> new ArrayList<>());
        items.add(item);
        userItemIndex.put(item.getOwner().getId(), items);
        return item;
    }

    public Optional<Item> findById(Long itemId) {
        return Optional.ofNullable(repo.get(itemId));
    }

    public void deleteById(Long itemId) {
        if (repo.containsKey(itemId)) {
            Item item = repo.get(itemId);
            Long ownerId = item.getOwner().getId();
            List<Item> itemsByUser = userItemIndex.get(ownerId);
            itemsByUser.remove(item);
            userItemIndex.put(ownerId, itemsByUser);

            repo.remove(itemId);
        } else {
            throw new NotFoundObjectException("Данного объекта нет в базе.");
        }
    }

    public void deleteAll() {
        repo.clear();
        userItemIndex.clear();
    }

    public List<Item> findItemsByOwner(Long ownerId) {
        return userItemIndex.get(ownerId);
    }

}
