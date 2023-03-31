package ru.practicum.shareit.item.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.comment.CommentService;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ItemController {
    ItemService itemService;
    ItemMapper itemMapper;
    CommentMapper commentMapper;
    CommentService commentService;
    BookingService bookingService;
    BookingMapper bookingMapper;
    UserService userService;


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
    public ItemDtoForOwner findById(@PathVariable("itemId") Long itemId,
                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        Item item = itemService.findById(itemId);
        return mapToItemDtoForOwner(item, userId);
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
        List<ItemDtoForOwner> result = new ArrayList<>();
        for (Item item : itemsByOwner) {
            ItemDtoForOwner itenDto = mapToItemDtoForOwner(item, userId);
            result.add(itenDto);
        }

        return result;
    }

    private ItemDtoForOwner mapToItemDtoForOwner(Item item, Long userId) {
        List<CommentDto> comments = commentMapper.mapToDtoList(commentService.findAllByItemId(item.getId()));

        Booking lastBookingByOwner = bookingService.findLastBookingByItemId(item.getId());
        BookingDto lastBooking;
        if (bookingService.checkAccessForItemLastNextBookingByUserId(lastBookingByOwner, userId)) {
            lastBooking = bookingMapper.fromBookingToDto(lastBookingByOwner);
        } else {
            lastBooking = null;
        }

        Booking nextBookingByOwner = bookingService.findNextBookingByItemId(item.getId());
        BookingDto nextBooking;
        if (bookingService.checkAccessForItemLastNextBookingByUserId(nextBookingByOwner, userId)) {
            nextBooking = bookingMapper.fromBookingToDto(nextBookingByOwner);
        } else {
            nextBooking = null;
        }

        return itemMapper.fromItemToDtoForOwner(item, comments, lastBooking, nextBooking);
    }

    @GetMapping("/search")
    public List<ItemDto> findItemsBySearch(@RequestParam String text) {
        return itemMapper.mapToDtoList(itemService.findItemsBySearch(text));
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@PathVariable("itemId") Long itemId,
                                 @RequestHeader("X-Sharer-User-Id") Long userId,
                                 @Valid @RequestBody CommentDto commentDto) {
        Item item = itemService.findById(itemId);
        User author = userService.findById(userId);
        Comment newComment = commentMapper.fromDtoToComment(commentDto, item, author);
        return commentMapper.fromCommentToDto(itemService.addComment(itemId, userId, newComment));
    }

}
