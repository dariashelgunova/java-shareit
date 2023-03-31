package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;
import ru.practicum.shareit.item.dto.ItemDtoWithComments;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemMapper {
    public Item fromDtoToItem(ItemDto itemDto) {
        if (itemDto == null) return null;

        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());

        return item;
    }

    public ItemDto fromItemToDto(Item item) {
        if (item == null) return null;

        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());

        return itemDto;
    }

    public List<ItemDto> mapToDtoList(List<Item> items) {
        if (items == null) return null;

        return items
                .stream()
                .map(this::fromItemToDto)
                .collect(Collectors.toList());
    }

    public ItemDtoWithComments fromItemToDtoWithComments(Item item, List<CommentDto> comments) {
        if (item == null) return null;

        ItemDtoWithComments itemDto = new ItemDtoWithComments();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setComments(comments);

        return itemDto;
    }

    public ItemDtoForOwner fromItemToDtoForOwner(Item item, List<CommentDto> comments, BookingDto lastBooking, BookingDto nextBooking) {
        if (item == null) return null;

        ItemDtoForOwner itemDto = new ItemDtoForOwner();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setComments(comments);
        itemDto.setLastBooking(lastBooking);
        itemDto.setNextBooking(nextBooking);

        return itemDto;
    }
}
