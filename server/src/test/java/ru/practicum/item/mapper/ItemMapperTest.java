package ru.practicum.item.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.item.dto.ItemDtoForOwner;
import ru.practicum.item.dto.ItemRequestDto;
import ru.practicum.item.dto.ItemSimpleDto;
import ru.practicum.item.model.Item;
import ru.practicum.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ItemMapperTest {

    @Test
    void fromItemRequestDtoTest() {
        ItemRequestDto dto = new ItemRequestDto(1L, "name", "description", true, null);

        Item item = ItemMapper.fromItemRequestDto(dto, null);

        assertEquals(dto.getId(), item.getId());
        assertEquals(dto.getName(), item.getName());
        assertEquals(dto.getDescription(), item.getDescription());
        assertEquals(dto.getAvailable(), item.getAvailable());

        dto = null;
        assertNull(ItemMapper.fromItemRequestDto(dto, null));
    }

    @Test
    void toItemRequestDtoTest() {
        User owner = new User(1L, "yandex@yandex.ru", "name");
        Item item = new Item(1L, "name", "description", true, owner, null, null, null, null);

        ItemRequestDto dto = ItemMapper.toItemRequestDto(item);
        assertEquals(item.getId(), dto.getId());
        assertEquals(item.getName(), dto.getName());
        assertEquals(item.getDescription(), dto.getDescription());
        assertEquals(item.getAvailable(), dto.getAvailable());

        item = null;
        assertNull(ItemMapper.toItemRequestDto(item));
    }

    @Test
    void toItemRequestDtoListTest() {
        User owner = new User(1L, "yandex@yandex.ru", "name");
        Item item = new Item(1L, "name", "description", true, owner, null, null, null, null);
        List<Item> items = List.of(item);

        List<ItemRequestDto> dtos = ItemMapper.toItemRequestDtoList(items);

        assertEquals(items.size(), dtos.size());

        items = null;
        assertNull(ItemMapper.toItemRequestDtoList(items));
    }

    @Test
    void toItemDtoForOwnerTest() {
        User owner = new User(1L, "yandex@yandex.ru", "name");
        Item item = new Item(1L, "name", "description", true, owner, null, null, null, null);

        ItemDtoForOwner dto = ItemMapper.toItemDtoForOwner(item);
        assertEquals(item.getId(), dto.getId());
        assertEquals(item.getName(), dto.getName());
        assertEquals(item.getDescription(), dto.getDescription());
        assertEquals(item.getAvailable(), dto.getAvailable());

        item = null;
        assertNull(ItemMapper.toItemDtoForOwner(item));
    }

    @Test
    void toItemDtoForOwnerListTest() {
        User owner = new User(1L, "yandex@yandex.ru", "name");
        Item item = new Item(1L, "name", "description", true, owner, null, null, null, null);
        List<Item> items = List.of(item);

        List<ItemDtoForOwner> dtos = ItemMapper.toItemDtoForOwnerList(items);

        assertEquals(items.size(), dtos.size());

        items = null;
        assertNull(ItemMapper.toItemDtoForOwnerList(items));
    }

    @Test
    void toItemSimpleDtoTest() {
        User owner = new User(1L, "yandex@yandex.ru", "name");
        Item item = new Item(1L, "name", "description", true, owner, null, null, null, null);

        ItemSimpleDto dto = ItemMapper.toItemSimpleDto(item);
        assertEquals(item.getId(), dto.getId());
        assertEquals(item.getName(), dto.getName());

        item = null;
        assertNull(ItemMapper.toItemSimpleDto(item));
    }
}