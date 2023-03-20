package ru.practicum.shareit.item.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundObjectException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.ItemRepo;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemServiceImpl implements ItemService {
    ItemRepo itemRepo;
    UserService userService;

    public List<Item> findAll() {
        return itemRepo.findAll();
    }

    public Item create(Item newItem, Long ownerId) {
        User owner = userService.findById(ownerId);
        newItem.setOwner(owner);
        return itemRepo.create(newItem);
    }

    public Item update(Item newItem, Long ownerId, Long itemId) {
        Item oldItem = getItemByIdOrThrowException(itemId);
        if (Objects.equals(oldItem.getOwner().getId(), ownerId)) {
            return changeItemFields(oldItem, newItem);
        } else {
            throw new AccessDeniedException("Только собственник может редактировать вещь!");
        }
    }


    public Item findById(Long itemId) {
        return getItemByIdOrThrowException(itemId);
    }

    public void deleteById(Long itemId) {
        itemRepo.deleteById(itemId);
    }

    public void deleteAll() {
        itemRepo.deleteAll();
    }

    public List<Item> findItemsByOwner(Long ownerId) {
        return itemRepo.findItemsByOwner(ownerId);
    }

    public List<Item> findItemsBySearch(String requestText) {
        if (StringUtils.isBlank(requestText)) return Collections.emptyList();
        List<Item> result =  itemRepo.findAll()
                .stream()
                .filter(o -> (o.getName().toLowerCase().contains(requestText.toLowerCase())
                        || o.getDescription().toLowerCase().contains(requestText.toLowerCase()))
                        && o.getAvailable())
                .collect(Collectors.toList());
        if (result.isEmpty()) {
            throw new NotFoundObjectException("По вашему запросу ничего найдено.");
        } else {
            return result;
        }
    }

    private Item getItemByIdOrThrowException(Long itemId) {
        return itemRepo.findById(itemId)
                .orElseThrow(() -> new NotFoundObjectException("Объект не был найден"));
    }

    private Item changeItemFields(Item oldItem, Item newItem) {
        if (newItem.getName() != null && !newItem.getName().isBlank()) {
            oldItem.setName(newItem.getName());
        }
        if (newItem.getDescription() != null && !newItem.getDescription().isBlank()) {
            oldItem.setDescription(newItem.getDescription());
        }
        if (newItem.getAvailable() != null) {
            oldItem.setAvailable(newItem.getAvailable());
        }
        return oldItem;
    }
}
