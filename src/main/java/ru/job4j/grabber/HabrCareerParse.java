package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.time.LocalDateTime;

public class HabrCareerParse {

    private static final String SOURCE_LINK = "https://career.habr.com";

    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer?page=", SOURCE_LINK);

    public static void main(String[] args) throws IOException {
        for (int i = 1; i <= 5; i++) {
            Connection connection = Jsoup.connect("%s%d".formatted(PAGE_LINK, i));
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                String vacancyName = titleElement.text();
                Element dateElement = row.select(".vacancy-card__date").first();
                Element dateTimeRow = dateElement.child(0);
                String vacancyDateTime = dateTimeRow.attr("datetime");
                LocalDateTime vacancyDateTimeFormatted = new HabrCareerDateTimeParser().parse(vacancyDateTime);
                String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                System.out.printf("%s %s %s%n", vacancyName, link, vacancyDateTimeFormatted);
            });
        }
    }
}

