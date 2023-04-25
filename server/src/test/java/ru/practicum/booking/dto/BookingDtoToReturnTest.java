package ru.practicum.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.booking.model.Status;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoToReturnTest {

    @Autowired
    private JacksonTester<BookingDtoToReturn> json;

    @Test
    public void testSerialize() throws Exception {
        LocalDateTime start = LocalDateTime.of(2023, 4, 30, 10, 10, 10);
        LocalDateTime end = LocalDateTime.of(2023, 4, 30, 15, 10, 10);
        BookingDtoToReturn details = new BookingDtoToReturn(1L, start, end, null, null, Status.APPROVED);
        assertThat(this.json.write(details)).hasJsonPathNumberValue("@.id");
        assertThat(this.json.write(details)).hasJsonPathStringValue("@.start");
        assertThat(this.json.write(details)).hasJsonPathStringValue("@.end");
        assertThat(this.json.write(details)).hasJsonPathStringValue("@.status");
    }

}