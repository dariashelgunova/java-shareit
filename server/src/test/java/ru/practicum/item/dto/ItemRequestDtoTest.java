package ru.practicum.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    public void testSerialize() throws Exception {
        ItemRequestDto details = new ItemRequestDto(1L, "name", "description", true, 1L);
        assertThat(this.json.write(details)).hasJsonPathNumberValue("@.id");
        assertThat(this.json.write(details)).hasJsonPathStringValue("@.name");
        assertThat(this.json.write(details)).hasJsonPathStringValue("@.description");
        assertThat(this.json.write(details)).hasJsonPathBooleanValue("@.available");
        assertThat(this.json.write(details)).hasJsonPathNumberValue("@.requestId");
    }

    @Test
    public void testDeserialize() throws Exception {
        String content = "{\"id\":1,\"name\":\"name\", \"description\":\"description\",\"available\":\"true\",\"requestId\":1}";
        ItemRequestDto actual = this.json.parseObject(content);
        assertThat(actual).isEqualTo(new ItemRequestDto(1L, "name", "description", true, 1L));
        assertThat(actual.getName()).isEqualTo("name");
        assertThat(actual.getDescription()).isEqualTo("description");
        assertThat(actual.getAvailable()).isEqualTo(true);
        assertThat(actual.getId()).isEqualTo(1);
        assertThat(actual.getRequestId()).isEqualTo(1);
    }

}