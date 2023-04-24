package ru.practicum.item.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.client.ItemClient;
import ru.practicum.item.comment.dto.CommentRequestDto;
import ru.practicum.item.dto.ItemRequestDto;
import ru.practicum.validation.Create;
import ru.practicum.validation.Update;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collections;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ItemController {
    ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@Validated(Create.class) @RequestBody ItemRequestDto item,
                                         @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.create(item, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@PathVariable("itemId") Long itemId,
                                         @Validated(Update.class) @RequestBody ItemRequestDto item,
                                         @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.update(itemId, item, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findById(@PathVariable("itemId") Long itemId,
                                           @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.findById(itemId, userId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteById(@PathVariable("itemId") Long itemId) {
        itemClient.deleteById(itemId);
    }

    @DeleteMapping
    public void deleteAll() {
        itemClient.deleteAll();
    }

    @GetMapping
    public ResponseEntity<Object> findItemsByOwner(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(defaultValue = "10") Integer size,
                                                   @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.findItemsByOwner(from, size, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findItemsBySearch(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                    @Positive @RequestParam(defaultValue = "10") Integer size,
                                                    @RequestParam String text,
                                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        if (text.isBlank()) {
            return new ResponseEntity<Object>(Collections.EMPTY_LIST, HttpStatus.OK);
        }
        return itemClient.findItemsBySearch(from, size, text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@PathVariable("itemId") Long itemId,
                                                @RequestHeader("X-Sharer-User-Id") Long userId,
                                                @Valid @RequestBody CommentRequestDto commentDto) {
        return itemClient.createComment(itemId, userId, commentDto);
    }
}
