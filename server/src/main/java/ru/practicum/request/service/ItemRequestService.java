package ru.practicum.request.service;

import ru.practicum.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {

    List<ItemRequest> findAll(Long userId);

    ItemRequest create(ItemRequest itemRequestDto, Long userId);

    List<ItemRequest> findRequestsCreatedByUsers(Integer from, Integer size, Long userId);

    ItemRequest findById(Long requestId, Long userId);

    ItemRequest checkIfRequestIdIsNotNull(Long requestId, Long userId);
}
