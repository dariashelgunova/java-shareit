package ru.practicum.shareit.item.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemController {
    ItemService itemService;
    ItemMapper itemMapper;

    @PostMapping
    public ItemDto create(@Validated(Create.class) @RequestBody ItemDto item, @RequestHeader("X-Sharer-User-Id") Long userId) {
        Item newItem = itemMapper.fromDtoToItem(item);
        Item createdItem = itemService.create(newItem, userId);
        return itemMapper.fromItemToDto(createdItem);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable("itemId") Long itemId, @Validated(Update.class) @RequestBody ItemDto item,
                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        Item newItem = itemMapper.fromDtoToItem(item);
        Item updatedItem = itemService.update(newItem, userId, itemId);
        return itemMapper.fromItemToDto(updatedItem);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable("itemId") Long itemId) {
        Item item = itemService.findById(itemId);
        return itemMapper.fromItemToDto(item);
    }

    @DeleteMapping("/{itemId}")
    public void deleteById(@PathVariable("itemId") Long itemId) {
        itemService.deleteById(itemId);
    }

    @DeleteMapping
    public void deleteAll() {
        itemService.deleteAll();
    }

    @GetMapping
    public List<ItemDto> findItemsByOwner(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemMapper.mapToDtoList(itemService.findItemsByOwner(userId));
    }

    @GetMapping("/search")
    public List<ItemDto> findItemsBySearch(@RequestParam String text) {
        return itemMapper.mapToDtoList(itemService.findItemsBySearch(text));
    }

}
