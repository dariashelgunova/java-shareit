package ru.practicum.request.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.request.dto.ItemRequestRequestDto;
import ru.practicum.request.dto.ItemRequestToReturnDto;
import ru.practicum.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.request.mapper.ItemRequestMapper.*;

class ItemRequestMapperTest {

    @Test
    void fromItemRequestRequestDtoTest() {
        ItemRequestRequestDto dto = new ItemRequestRequestDto(1L, "description");
        ItemRequest request = fromItemRequestRequestDto(dto);

        assertEquals(dto.getId(), request.getId());
        assertNotNull(request.getCreated());
        assertEquals(dto.getDescription(), request.getDescription());

        dto = null;
        assertNull(fromItemRequestRequestDto(dto));
    }

    @Test
    void toItemRequestToReturnDtoTest() {
        ItemRequest request = new ItemRequest(1L, "description", LocalDateTime.now(), null, Collections.emptyList());

        ItemRequestToReturnDto dto = toItemRequestToReturnDto(request);

        assertEquals(request.getId(), dto.getId());
        assertEquals(request.getDescription(), dto.getDescription());
        assertEquals(request.getCreated(), dto.getCreated());

        request = null;
        assertNull(toItemRequestToReturnDto(request));
    }

    @Test
    void toItemRequestToReturnDtoListTest() {
        ItemRequest request = new ItemRequest(1L, "description", LocalDateTime.now(), null, Collections.emptyList());
        List<ItemRequest> requests = List.of(request);

        List<ItemRequestToReturnDto> dtos = toItemRequestToReturnDtoList(requests);

        assertEquals(requests.size(), dtos.size());

        requests = null;
        assertNull(toItemRequestToReturnDtoList(requests));
    }
}