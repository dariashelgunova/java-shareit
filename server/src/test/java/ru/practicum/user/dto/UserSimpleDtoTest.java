package ru.practicum.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserSimpleDtoTest {

    @Autowired
    private JacksonTester<UserSimpleDto> json;

    @Test
    public void testSerialize() throws Exception {
        UserSimpleDto details = new UserSimpleDto(1L);
        assertThat(this.json.write(details)).hasJsonPathNumberValue("@.id");
    }

}