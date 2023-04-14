package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingSimpleDtoTest {

    @Autowired
    private JacksonTester<BookingSimpleDto> json;

    @Test
    public void testSerialize() throws Exception {
        BookingSimpleDto details = new BookingSimpleDto(1L, 1L);
        assertThat(this.json.write(details)).hasJsonPathNumberValue("@.id");
        assertThat(this.json.write(details)).hasJsonPathNumberValue("@.bookerId");
    }

}