package ru.practicum.item.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.comment.dto.CommentDtoToReturn;
import ru.practicum.item.comment.dto.CommentRequestDto;
import ru.practicum.item.comment.model.Comment;
import ru.practicum.item.dto.ItemDtoForOwner;
import ru.practicum.item.dto.ItemRequestDto;
import ru.practicum.item.mapper.ItemMapper;
import ru.practicum.item.model.Item;
import ru.practicum.item.service.ItemService;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.request.service.ItemRequestService;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.item.comment.mapper.CommentMapper.fromCommentRequestDto;
import static ru.practicum.item.comment.mapper.CommentMapper.toCommentRequestDto;

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
    public ItemRequestDto create(@RequestBody ItemRequestDto item,
                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        ItemRequest request = itemRequestService.checkIfRequestIdIsNotNull(item.getRequestId(), userId);
        Item newItem = ItemMapper.fromItemRequestDto(item, request);
        Item createdItem = itemService.create(newItem, userId);
        return ItemMapper.toItemRequestDto(createdItem);
    }

    @PatchMapping("/{itemId}")
    public ItemRequestDto update(@PathVariable("itemId") Long itemId,
                                 @RequestBody ItemRequestDto item,
                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        ItemRequest request = itemRequestService.checkIfRequestIdIsNotNull(item.getRequestId(), userId);
        Item newItem = ItemMapper.fromItemRequestDto(item, request);
        Item updatedItem = itemService.update(newItem, userId, itemId);
        return ItemMapper.toItemRequestDto(updatedItem);
    }

    @GetMapping("/{itemId}")
    public ItemDtoForOwner findById(@PathVariable("itemId") Long itemId,
                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        Item item = itemService.findById(itemId);
        return ItemMapper.toItemDtoForOwner(itemService.addCommentsAndBookingsToItem(item, userId));
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
    public List<ItemDtoForOwner> findItemsByOwner(@RequestParam(defaultValue = "0") Integer from,
                                                  @RequestParam(defaultValue = "10") Integer size,
                                                  @RequestHeader("X-Sharer-User-Id") Long userId) {
        List<Item> itemsByOwner = itemService.findItemsByOwner(userId, from, size);
        return ItemMapper.toItemDtoForOwnerList(itemService.addCommentsAndBookingsToItems(itemsByOwner, userId));
    }

    @GetMapping("/search")
    public List<ItemRequestDto> findItemsBySearch(@RequestParam(defaultValue = "0") Integer from,
                                                  @RequestParam(defaultValue = "10") Integer size,
                                                  @RequestParam String text) {
        return ItemMapper.toItemRequestDtoList(itemService.findItemsBySearch(text, from, size));
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
