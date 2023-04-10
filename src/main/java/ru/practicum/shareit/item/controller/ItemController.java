package ru.practicum.shareit.item.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDtoToReturn;
import ru.practicum.shareit.item.comment.dto.CommentRequestDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.item.comment.mapper.CommentMapper.fromCommentRequestDto;
import static ru.practicum.shareit.item.comment.mapper.CommentMapper.toCommentRequestDto;
import static ru.practicum.shareit.item.mapper.ItemMapper.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ItemController {
    ItemService itemService;
    UserService userService;

    ItemRequestService itemRequestService;


    @PostMapping
    public ItemRequestDto create(@Validated(Create.class) @RequestBody ItemRequestDto item,
                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        ItemRequest request = itemRequestService.checkIfRequestIdIsNotNull(item.getRequestId(), userId);
        Item newItem = fromItemRequestDto(item, request);
        Item createdItem = itemService.create(newItem, userId);
        return toItemRequestDto(createdItem);
    }

    @PatchMapping("/{itemId}")
    public ItemRequestDto update(@PathVariable("itemId") Long itemId,
                                 @Validated(Update.class) @RequestBody ItemRequestDto item,
                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        ItemRequest request = itemRequestService.checkIfRequestIdIsNotNull(item.getRequestId(), userId);
        Item newItem = fromItemRequestDto(item, request);
        Item updatedItem = itemService.update(newItem, userId, itemId);
        return toItemRequestDto(updatedItem);
    }

    @GetMapping("/{itemId}")
    public ItemDtoForOwner findById(@PathVariable("itemId") Long itemId,
                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        Item item = itemService.findById(itemId);
        return toItemDtoForOwner(itemService.addCommentsAndBookingsToItem(item, userId));
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
    public List<ItemDtoForOwner> findItemsByOwner(@RequestParam(defaultValue = "-1") Integer from,
                                                  @RequestParam(defaultValue = "-1") Integer size,
                                                  @RequestHeader("X-Sharer-User-Id") Long userId) {
        List<Item> itemsByOwner = itemService.findItemsByOwner(userId, from, size);
        return toItemDtoForOwnerList(itemService.addCommentsAndBookingsToItems(itemsByOwner, userId));
    }

    @GetMapping("/search")
    public List<ItemRequestDto> findItemsBySearch(@RequestParam(defaultValue = "-1") Integer from,
                                                  @RequestParam(defaultValue = "-1") Integer size,
                                                  @RequestParam String text) {
        return toItemRequestDtoList(itemService.findItemsBySearch(text, from, size));
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoToReturn createComment(@PathVariable("itemId") Long itemId,
                                            @RequestHeader("X-Sharer-User-Id") Long userId,
                                            @Valid @RequestBody CommentRequestDto commentDto) {
        Item item = itemService.findById(itemId);
        User author = userService.findById(userId);
        Comment newComment = fromCommentRequestDto(commentDto, item, author);
        return toCommentRequestDto(itemService.addComment(itemId, userId, newComment));
    }

}
