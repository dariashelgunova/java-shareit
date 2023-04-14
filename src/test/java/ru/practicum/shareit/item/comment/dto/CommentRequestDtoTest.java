package ru.practicum.shareit.item.comment.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentRequestDtoTest {

    @Autowired
    private JacksonTester<CommentRequestDto> json;

    @Test
    public void testDeserialize() throws Exception {
        String content = "{\"id\":1,\"text\":\"text\"}";
        CommentRequestDto actual = this.json.parseObject(content);
        assertThat(actual).isEqualTo(new CommentRequestDto(1L, "text"));
    }

}