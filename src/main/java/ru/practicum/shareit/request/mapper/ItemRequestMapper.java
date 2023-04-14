package ru.practicum.shareit.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestToReturnDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.mapper.ItemMapper.toItemRequestDtoList;

@UtilityClass
public class ItemRequestMapper {

    public static ItemRequest fromItemRequestRequestDto(ItemRequestRequestDto itemRequestRequestDto) {
        if (itemRequestRequestDto == null) return null;
        LocalDateTime currentTime = LocalDateTime.from(LocalDateTime.now());

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(itemRequestRequestDto.getId());
        itemRequest.setDescription(itemRequestRequestDto.getDescription());
        itemRequest.setCreated(currentTime);

        return itemRequest;
    }

    public static ItemRequestToReturnDto toItemRequestToReturnDto(ItemRequest itemRequest) {
        if (itemRequest == null) return null;

        ItemRequestToReturnDto itemRequestToReturnDto = new ItemRequestToReturnDto();
        itemRequestToReturnDto.setId(itemRequest.getId());
        itemRequestToReturnDto.setDescription(itemRequest.getDescription());
        itemRequestToReturnDto.setCreated(itemRequest.getCreated());
        itemRequestToReturnDto.setItems(toItemRequestDtoList(itemRequest.getItems()));

        return itemRequestToReturnDto;
    }

    public static List<ItemRequestToReturnDto> toItemRequestToReturnDtoList(List<ItemRequest> itemRequests) {
        if (itemRequests == null) return null;

        return itemRequests
                .stream()
                .map(ItemRequestMapper::toItemRequestToReturnDto)
                .collect(Collectors.toList());
    }
}
