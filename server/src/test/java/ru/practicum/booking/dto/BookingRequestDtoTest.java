package ru.practicum.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingRequestDtoTest {
    @Autowired
    private JacksonTester<BookingRequestDto> json;

    @Test
    public void testDeserialize() throws Exception {
        LocalDateTime start = LocalDateTime.of(2023, 4, 30, 10, 10, 10);
        LocalDateTime end = LocalDateTime.of(2023, 4, 30, 15, 10, 10);
        String content = "{\"id\":1,\"start\":\"2023-04-30T10:10:10\", \"end\":\"2023-04-30T15:10:10\", \"itemId\":1}";
        BookingRequestDto actual = this.json.parseObject(content);
        assertThat(actual).isEqualTo(new BookingRequestDto(1L, start, end, 1L));
        assertThat(actual.getId()).isEqualTo(1);
        assertThat(actual.getStart()).isEqualTo(start);
        assertThat(actual.getEnd()).isEqualTo(end);
        assertThat(actual.getItemId()).isEqualTo(1);
    }

}