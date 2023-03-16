package ru.practicum.shareit.item.repo;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRepoInMemory implements ItemRepo {
    final Map<Long, Item> repo = new HashMap<>();
    Long idCounter = 0L;

    public List<Item> findAll() {
        return new ArrayList<Item>(repo.values());
    }

    public Item create(Item item) {
        item.setId(++idCounter);
        repo.put(idCounter, item);
        return item;
    }

    public Item update(Item item) {
        Item oldItem = repo.get(item.getId());
        oldItem.setName(item.getName());
        oldItem.setDescription(item.getDescription());
        oldItem.setAvailable(item.getAvailable());
        return oldItem;
    }

    public Optional<Item> findById(Long itemId) {
        return Optional.ofNullable(repo.get(itemId));
    }

    public void deleteById(Long itemId) {
        repo.remove(itemId);
    }

    public void deleteAll() {
        repo.clear();
    }

}
