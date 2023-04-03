package ru.practicum.shareit.item.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDtoToReturn;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.mapper.Mapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ItemController {
    ItemService itemService;
    Mapper mapper;
    UserService userService;


    @PostMapping
    public ItemRequestDto create(@Validated(Create.class) @RequestBody ItemRequestDto item, @RequestHeader("X-Sharer-User-Id") Long userId) {
        Item newItem = mapper.fromItemRequestDto(item);
        Item createdItem = itemService.create(newItem, userId);
        return mapper.toItemRequestDto(createdItem);
    }

    @PatchMapping("/{itemId}")
    public ItemRequestDto update(@PathVariable("itemId") Long itemId, @Validated(Update.class) @RequestBody ItemRequestDto item,
                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        Item newItem = mapper.fromItemRequestDto(item);
        Item updatedItem = itemService.update(newItem, userId, itemId);
        return mapper.toItemRequestDto(updatedItem);
    }

    @GetMapping("/{itemId}")
    public ItemDtoForOwner findById(@PathVariable("itemId") Long itemId,
                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        Item item = itemService.findById(itemId);
        return mapper.toItemDtoForOwner(itemService.addCommentsAndBookingsToItem(item, userId));
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
    public List<ItemDtoForOwner> findItemsByOwner(@RequestHeader("X-Sharer-User-Id") Long userId) {
        List<Item> itemsByOwner = itemService.findItemsByOwner(userId);
        return mapper.toItemDtoForOwnerList(itemService.addCommentsAndBookingsToItems(itemsByOwner, userId));
    }

    @GetMapping("/search")
    public List<ItemRequestDto> findItemsBySearch(@RequestParam String text) {
        return mapper.toItemRequestDtoList(itemService.findItemsBySearch(text));
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoToReturn createComment(@PathVariable("itemId") Long itemId,
                                            @RequestHeader("X-Sharer-User-Id") Long userId,
                                            @Valid @RequestBody CommentDtoToReturn commentDtoToReturn) {
        Item item = itemService.findById(itemId);
        User author = userService.findById(userId);
        Comment newComment = mapper.fromCommentRequestDto(commentDtoToReturn, item, author);
        return mapper.toCommentRequestDto(itemService.addComment(itemId, userId, newComment));
    }

}
