package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestToReturnDtoTest {

    @Autowired
    private JacksonTester<ItemRequestToReturnDto> json;

    @Test
    public void testSerialize() throws Exception {
        ItemRequestToReturnDto details = new ItemRequestToReturnDto(1L, "description", LocalDateTime.now(), Collections.emptyList());
        assertThat(this.json.write(details)).hasJsonPathNumberValue("@.id");
        assertThat(this.json.write(details)).hasJsonPathStringValue("@.description");
        assertThat(this.json.write(details)).hasJsonPathStringValue("@.created");
        assertThat(this.json.write(details)).hasJsonPathArrayValue("@.items");
    }

}