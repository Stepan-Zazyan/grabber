package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {
    private static String sourceLink = "https://career.habr.com";

    private static String pageLink = String.format("%s/vacancies/java_developer?page=", sourceLink);

    private final DateTimeParser dateTimeParser;

    private List<Post> list = new ArrayList<>();

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    public static void main(String[] args) throws IOException {
        DateTimeParser dateTimeParser = new HabrCareerDateTimeParser();
        HabrCareerParse habrCareerParse = new HabrCareerParse(dateTimeParser);
        for (int i = 1; i <= 1; i++) {
            Connection connection = Jsoup.connect("%s%d".formatted(pageLink, i));
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                String vacancyName = titleElement.text();
                Element dateElement = row.select(".vacancy-card__date").first();
                Element dateTimeRow = dateElement.child(0);
                String vacancyDateTime = dateTimeRow.attr("datetime");
                LocalDateTime vacancyDateTimeFormatted = dateTimeParser.parse(vacancyDateTime);
                String vacancyLink = String.format("%s%s", sourceLink, linkElement.attr("href"));
                String link = String.format("%s%s", sourceLink, vacancyLink);
/*
                System.out.printf("%s %s %s%n", vacancyName, link, vacancyDateTimeFormatted);
*/
                Connection descriptionConnection = Jsoup.connect(vacancyLink);
                try {
                    Document descriptionDocument = descriptionConnection.get();
                    Element descriptionTitleElement = descriptionDocument.select(".vacancy-description__text").first();
                    String vacancyDescription = descriptionTitleElement.text();
                    habrCareerParse.list(vacancyName, link, vacancyDescription, vacancyDateTimeFormatted);
/*
                    System.out.println(vacancyDescription);
*/
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            System.out.println(habrCareerParse.getList());
        }
    }

    public static String getSourceLink() {
        return sourceLink;
    }

    public static String getPageLink() {
        return pageLink;
    }

    public HabrCareerParse parseInfo(HabrCareerParse habrCareerParse, String sourceLink, String pageLink,
                                String blokBegining, String titleBlock, String dateBlock, String vacancyDateTimeValue,
                                String vacancyDescriptionBlock, int pages) throws IOException {
        for (int i = 1; i <= pages; i++) {
            Connection connection = Jsoup.connect("%s%d".formatted(pageLink, i));
            Document document = connection.get();
            Elements rows = document.select(blokBegining);
            rows.forEach(row -> {
                Element titleElement = row.select(titleBlock).first();
                Element linkElement = titleElement.child(0);
                String vacancyName = titleElement.text();
                Element dateElement = row.select(dateBlock).first();
                Element dateTimeRow = dateElement.child(0);
                String vacancyDateTime = dateTimeRow.attr(vacancyDateTimeValue);
                LocalDateTime vacancyDateTimeFormatted = dateTimeParser.parse(vacancyDateTime);
                String vacancyLink = String.format("%s%s", sourceLink, linkElement.attr("href"));
                String link = String.format("%s%s", sourceLink, vacancyLink);
                Connection descriptionConnection = Jsoup.connect(vacancyLink);
                try {
                    Document descriptionDocument = descriptionConnection.get();
                    Element descriptionTitleElement = descriptionDocument.select(vacancyDescriptionBlock).first();
                    String vacancyDescription = descriptionTitleElement.text();
                   habrCareerParse.list(vacancyName, link, vacancyDescription, vacancyDateTimeFormatted);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return habrCareerParse;
    }

    @Override
    public List<Post> list(String title, String link, String description, LocalDateTime vacancyDateTime) {
        Post post = new Post(title, link, description, vacancyDateTime);
        list.add(post);
        return list;
    }

    public List<Post> getList() {
        return list;
    }
}

