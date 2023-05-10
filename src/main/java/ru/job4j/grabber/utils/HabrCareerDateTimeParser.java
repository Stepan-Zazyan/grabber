package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class HabrCareerDateTimeParser implements DateTimeParser {

    @Override
    public LocalDateTime parse(String parse) {
        if (Objects.equals(parse, "")) {
            throw new DateTimeParseException("Строка с датой", "", 0);
        }
        return OffsetDateTime.parse(parse).toLocalDateTime();
    }
}
