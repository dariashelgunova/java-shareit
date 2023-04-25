package ru.practicum.request.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.ItemRequestRequestDto;
import ru.practicum.request.dto.ItemRequestToReturnDto;
import ru.practicum.request.mapper.ItemRequestMapper;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemRequestController {

    ItemRequestService itemRequestService;

    @GetMapping
    public List<ItemRequestToReturnDto> findAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return ItemRequestMapper.toItemRequestToReturnDtoList(itemRequestService.findAll(userId));
    }

    @PostMapping
    public ItemRequestToReturnDto create(@RequestBody ItemRequestRequestDto itemRequestDto,
                                         @RequestHeader("X-Sharer-User-Id") Long userId) {
        ItemRequest itemRequest = ItemRequestMapper.fromItemRequestRequestDto(itemRequestDto);
        ItemRequest createdRequest = itemRequestService.create(itemRequest, userId);
        return ItemRequestMapper.toItemRequestToReturnDto(createdRequest);
    }

    @GetMapping("/all")
    public List<ItemRequestToReturnDto> findRequestsCreatedByUsers(@RequestParam(defaultValue = "0") Integer from,
                                                                   @RequestParam(defaultValue = "10") Integer size,
                                                                   @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ItemRequestMapper.toItemRequestToReturnDtoList(itemRequestService.findRequestsCreatedByUsers(from, size, userId));
    }

    @GetMapping("/{requestId}")
    public ItemRequestToReturnDto findRequestById(@PathVariable("requestId") Long requestId,
                                                  @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ItemRequestMapper.toItemRequestToReturnDto(itemRequestService.findById(requestId, userId));
    }

}
