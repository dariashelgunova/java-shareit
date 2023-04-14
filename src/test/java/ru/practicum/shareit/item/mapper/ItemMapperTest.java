package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemSimpleDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.practicum.shareit.item.mapper.ItemMapper.*;

class ItemMapperTest {

    @Test
    void fromItemRequestDtoTest() {
        ItemRequestDto dto = new ItemRequestDto(1L, "name", "description", true, null);

        Item item = fromItemRequestDto(dto, null);

        assertEquals(dto.getId(), item.getId());
        assertEquals(dto.getName(), item.getName());
        assertEquals(dto.getDescription(), item.getDescription());
        assertEquals(dto.getAvailable(), item.getAvailable());

        dto = null;
        assertNull(fromItemRequestDto(dto, null));
    }

    @Test
    void toItemRequestDtoTest() {
        User owner = new User(1L, "yandex@yandex.ru", "name");
        Item item = new Item(1L, "name", "description", true, owner, null, null, null, null);

        ItemRequestDto dto = toItemRequestDto(item);
        assertEquals(item.getId(), dto.getId());
        assertEquals(item.getName(), dto.getName());
        assertEquals(item.getDescription(), dto.getDescription());
        assertEquals(item.getAvailable(), dto.getAvailable());

        item = null;
        assertNull(toItemRequestDto(item));
    }

    @Test
    void toItemRequestDtoListTest() {
        User owner = new User(1L, "yandex@yandex.ru", "name");
        Item item = new Item(1L, "name", "description", true, owner, null, null, null, null);
        List<Item> items = List.of(item);

        List<ItemRequestDto> dtos = toItemRequestDtoList(items);

        assertEquals(items.size(), dtos.size());

        items = null;
        assertNull(toItemRequestDtoList(items));
    }

    @Test
    void toItemDtoForOwnerTest() {
        User owner = new User(1L, "yandex@yandex.ru", "name");
        Item item = new Item(1L, "name", "description", true, owner, null, null, null, null);

        ItemDtoForOwner dto = toItemDtoForOwner(item);
        assertEquals(item.getId(), dto.getId());
        assertEquals(item.getName(), dto.getName());
        assertEquals(item.getDescription(), dto.getDescription());
        assertEquals(item.getAvailable(), dto.getAvailable());

        item = null;
        assertNull(toItemDtoForOwner(item));
    }

    @Test
    void toItemDtoForOwnerListTest() {
        User owner = new User(1L, "yandex@yandex.ru", "name");
        Item item = new Item(1L, "name", "description", true, owner, null, null, null, null);
        List<Item> items = List.of(item);

        List<ItemDtoForOwner> dtos = toItemDtoForOwnerList(items);

        assertEquals(items.size(), dtos.size());

        items = null;
        assertNull(toItemDtoForOwnerList(items));
    }

    @Test
    void toItemSimpleDtoTest() {
        User owner = new User(1L, "yandex@yandex.ru", "name");
        Item item = new Item(1L, "name", "description", true, owner, null, null, null, null);

        ItemSimpleDto dto = toItemSimpleDto(item);
        assertEquals(item.getId(), dto.getId());
        assertEquals(item.getName(), dto.getName());

        item = null;
        assertNull(toItemSimpleDto(item));
    }
}