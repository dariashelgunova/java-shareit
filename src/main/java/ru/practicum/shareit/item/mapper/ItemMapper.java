package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;
import ru.practicum.shareit.item.dto.ItemDtoWithComments;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemSimpleDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.mapper.BookingMapper.toBookingSimpleDto;
import static ru.practicum.shareit.item.comment.mapper.CommentMapper.toCommentSimpleDtoList;

@UtilityClass
public class ItemMapper {

    public static Item fromItemRequestDto(ItemRequestDto itemRequestDto) {
        if (itemRequestDto == null) return null;

        Item item = new Item();
        item.setId(itemRequestDto.getId());
        item.setName(itemRequestDto.getName());
        item.setDescription(itemRequestDto.getDescription());
        item.setAvailable(itemRequestDto.getAvailable());

        return item;
    }

    public static ItemRequestDto toItemRequestDto(Item item) {
        if (item == null) return null;

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(item.getId());
        itemRequestDto.setName(item.getName());
        itemRequestDto.setDescription(item.getDescription());
        itemRequestDto.setAvailable(item.getAvailable());

        return itemRequestDto;
    }

    public static List<ItemRequestDto> toItemRequestDtoList(List<Item> items) {
        if (items == null) return null;

        return items
                .stream()
                .map(ItemMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    public static ItemDtoWithComments toItemDtoWithComments(Item item, List<Comment> comments) {
        if (item == null) return null;

        ItemDtoWithComments itemDto = new ItemDtoWithComments();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setComments(toCommentSimpleDtoList(comments));

        return itemDto;
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

    public static List<ItemSimpleDto> toItemSimpleDtoList(List<Item> items) {
        if (items == null) return null;

        return items
                .stream()
                .map(ItemMapper::toItemSimpleDto)
                .collect(Collectors.toList());
    }

}
