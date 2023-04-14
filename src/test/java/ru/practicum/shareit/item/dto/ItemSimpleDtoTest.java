package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemSimpleDtoTest {

    @Autowired
    private JacksonTester<ItemSimpleDto> json;

    @Test
    public void testSerialize() throws Exception {
        ItemSimpleDto details = new ItemSimpleDto(1L, "name");
        assertThat(this.json.write(details)).hasJsonPathNumberValue("@.id");
        assertThat(this.json.write(details)).hasJsonPathStringValue("@.name");
    }

}