package ru.job4j.grabber;

import java.time.LocalDateTime;
import java.util.List;

public interface Parse {
    List<Post> list(String title, String link, String description, LocalDateTime vacancyDateTime);
}
