package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestRequestDtoTest {

    @Autowired
    private JacksonTester<ItemRequestRequestDto> json;

    @Test
    public void testDeserialize() throws Exception {
        String content = "{\"id\":1,\"description\":\"description\"}";
        ItemRequestRequestDto actual = this.json.parseObject(content);
        assertThat(actual).isEqualTo(new ItemRequestRequestDto(1L, "description"));
        assertThat(actual.getDescription()).isEqualTo("description");

    }

}