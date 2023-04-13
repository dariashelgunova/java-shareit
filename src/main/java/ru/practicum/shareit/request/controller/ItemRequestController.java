package ru.practicum.shareit.request.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestToReturnDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.shareit.request.mapper.ItemRequestMapper.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemRequestController {

    ItemRequestService itemRequestService;

    @GetMapping
    public List<ItemRequestToReturnDto> findAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return toItemRequestToReturnDtoList(itemRequestService.findAll(userId));
    }

    @PostMapping
    public ItemRequestToReturnDto create(@Valid @RequestBody ItemRequestRequestDto itemRequestDto,
                                         @RequestHeader("X-Sharer-User-Id") Long userId) {
        ItemRequest itemRequest = fromItemRequestRequestDto(itemRequestDto);
        ItemRequest createdRequest = itemRequestService.create(itemRequest, userId);
        return toItemRequestToReturnDto(createdRequest);
    }

    @GetMapping("/all")
    public List<ItemRequestToReturnDto> findRequestsCreatedByUsers(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                                   @Positive @RequestParam(defaultValue = "10") Integer size,
                                                                   @RequestHeader("X-Sharer-User-Id") Long userId) {
        return toItemRequestToReturnDtoList(itemRequestService.findRequestsCreatedByUsers(from, size, userId));
    }

    @GetMapping("/{requestId}")
    public ItemRequestToReturnDto findRequestById(@PathVariable("requestId") Long requestId,
                                                  @RequestHeader("X-Sharer-User-Id") Long userId) {
        return toItemRequestToReturnDto(itemRequestService.findById(requestId, userId));
    }

}
