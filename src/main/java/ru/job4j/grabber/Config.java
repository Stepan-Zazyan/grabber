package ru.job4j.grabber;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    public static Properties get() {
        Properties cfg = new Properties();
        try (
                InputStream in = Grabber.class.getClassLoader()
                        .getResourceAsStream("rabbit.properties")) {
            cfg.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return cfg;
    }

}
