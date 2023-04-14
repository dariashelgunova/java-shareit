package ru.practicum.shareit.item.comment.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentDtoToReturnTest {

    @Autowired
    private JacksonTester<CommentDtoToReturn> json;

    @Test
    public void testSerialize() throws Exception {
        CommentDtoToReturn details = new CommentDtoToReturn(1L, "text", null, "name", LocalDateTime.now());
        assertThat(this.json.write(details)).hasJsonPathNumberValue("@.id");
        assertThat(this.json.write(details)).hasJsonPathStringValue("@.text");
        assertThat(this.json.write(details)).hasJsonPathStringValue("@.authorName");
        assertThat(this.json.write(details)).hasJsonPathStringValue("@.created");
    }
}