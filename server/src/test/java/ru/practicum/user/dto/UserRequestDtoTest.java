package ru.practicum.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserRequestDtoTest {

    @Autowired
    private JacksonTester<UserRequestDto> json;

    @Test
    public void testSerialize() throws Exception {
        UserRequestDto details = new UserRequestDto(1L, "yandex@yandex.ru", "name");
        assertThat(this.json.write(details)).hasJsonPathNumberValue("@.id");
        assertThat(this.json.write(details)).hasJsonPathStringValue("@.email");
        assertThat(this.json.write(details)).hasJsonPathStringValue("@.name");
    }

    @Test
    public void testDeserialize() throws Exception {
        String content = "{\"id\":1,\"email\":\"yandex@yandex.ru\", \"name\":\"name\"}";
        UserRequestDto actual = this.json.parseObject(content);
        assertThat(actual).isEqualTo(new UserRequestDto(1L, "yandex@yandex.ru", "name"));
        assertThat(actual.getName()).isEqualTo("name");
        assertThat(actual.getEmail()).isEqualTo("yandex@yandex.ru");
    }

}