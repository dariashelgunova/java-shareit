package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoForOwnerTest {

    @Autowired
    private JacksonTester<ItemDtoForOwner> json;

    @Test
    public void testSerialize() throws Exception {
        ItemDtoForOwner details = new ItemDtoForOwner(1L, "name", "description", true, Collections.emptyList(), null, null);
        assertThat(this.json.write(details)).hasJsonPathNumberValue("@.id");
        assertThat(this.json.write(details)).hasJsonPathStringValue("@.name");
        assertThat(this.json.write(details)).hasJsonPathStringValue("@.description");
        assertThat(this.json.write(details)).hasJsonPathBooleanValue("@.available");
        assertThat(this.json.write(details)).hasJsonPathArrayValue("@.comments");
    }

}