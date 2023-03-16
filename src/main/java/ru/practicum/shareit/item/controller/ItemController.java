package ru.practicum.shareit.item.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemController {
    ItemServiceImpl itemServiceImpl;
    ItemMapper itemMapper;

    @PostMapping
    public ItemDto create(@Valid @RequestBody ItemDto item, @RequestHeader("X-Sharer-User-Id") Long userId) {
        Item newItem = itemMapper.fromDtoToItem(item);
        Item createdItem = itemServiceImpl.create(newItem, userId);
        return itemMapper.fromItemToDto(createdItem);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable("itemId") Long itemId, @RequestBody ItemDto item,
                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        Item newItem = itemMapper.fromDtoToItem(item);
        Item updatedItem = itemServiceImpl.update(newItem, userId, itemId);
        return itemMapper.fromItemToDto(updatedItem);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable("itemId") Long itemId) {
        Item item = itemServiceImpl.findById(itemId);
        return itemMapper.fromItemToDto(item);
    }

    @DeleteMapping("/{itemId}")
    public void deleteById(@PathVariable("itemId") Long itemId) {
        itemServiceImpl.deleteById(itemId);
    }

    @DeleteMapping
    public void deleteAll() {
        itemServiceImpl.deleteAll();
    }

    @GetMapping
    public List<ItemDto> findItemsByOwner(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemMapper.mapToDtoList(itemServiceImpl.findItemsByOwner(userId), this);
    }

    @GetMapping("/search")
    public List<ItemDto> findItemsBySearch(@RequestParam String text) {
        return itemMapper.mapToDtoList(itemServiceImpl.findItemsBySearch(text), this);
    }

}
