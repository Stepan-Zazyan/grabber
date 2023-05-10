package ru.job4j.grabber.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class HabrCareerDateTimeParserTest {

    @Test
    void parseCheck() {
        String inputDate = "2023-05-03T09:53:01+03:00";
        String outputDate = "2023-05-03T09:53:01";
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(inputDate);
        LocalDateTime localDateTime = offsetDateTime.toLocalDateTime();
        assertEquals(outputDate, String.valueOf(localDateTime));
    }

    @Test
    void emptyDate() {
        String inputDate = "";
        assertThatThrownBy(() -> OffsetDateTime.parse(inputDate))
                .isInstanceOf(DateTimeParseException.class);
    }
}