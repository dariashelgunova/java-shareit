package ru.practicum.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.item.dto.ItemDtoForOwner;
import ru.practicum.item.dto.ItemRequestDto;
import ru.practicum.item.dto.ItemSimpleDto;
import ru.practicum.item.model.Item;
import ru.practicum.request.model.ItemRequest;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.booking.mapper.BookingMapper.toBookingSimpleDto;
import static ru.practicum.item.comment.mapper.CommentMapper.toCommentSimpleDtoList;

@UtilityClass
public class ItemMapper {

    public static Item fromItemRequestDto(ItemRequestDto itemRequestDto, ItemRequest request) {
        if (itemRequestDto == null) return null;

        Item item = new Item();
        item.setId(itemRequestDto.getId());
        item.setName(itemRequestDto.getName());
        item.setDescription(itemRequestDto.getDescription());
        item.setAvailable(itemRequestDto.getAvailable());
        item.setRequest(request);

        return item;
    }

    public static ItemRequestDto toItemRequestDto(Item item) {
        if (item == null) return null;

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(item.getId());
        itemRequestDto.setName(item.getName());
        itemRequestDto.setDescription(item.getDescription());
        itemRequestDto.setAvailable(item.getAvailable());
        if (item.getRequest() != null) {
            itemRequestDto.setRequestId(item.getRequest().getId());
        }

        return itemRequestDto;
    }

    public static List<ItemRequestDto> toItemRequestDtoList(List<Item> items) {
        if (items == null) return null;

        return items
                .stream()
                .map(ItemMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    public static ItemDtoForOwner toItemDtoForOwner(Item item) {
        if (item == null) return null;

        ItemDtoForOwner itemDto = new ItemDtoForOwner();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setComments(toCommentSimpleDtoList(item.getComments()));
        itemDto.setLastBooking(toBookingSimpleDto(item.getLastBooking()));
        itemDto.setNextBooking(toBookingSimpleDto(item.getNextBooking()));

        return itemDto;
    }

    public static List<ItemDtoForOwner> toItemDtoForOwnerList(List<Item> items) {
        if (items == null) return null;

        return items
                .stream()
                .map(ItemMapper::toItemDtoForOwner)
                .collect(Collectors.toList());
    }

    public static ItemSimpleDto toItemSimpleDto(Item item) {
        if (item == null) return null;

        ItemSimpleDto itemDto = new ItemSimpleDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());

        return itemDto;
    }
}
