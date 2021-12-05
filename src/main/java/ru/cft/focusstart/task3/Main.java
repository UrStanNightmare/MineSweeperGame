package ru.cft.focusstart.task3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cft.focusstart.task3.app.Application;

public class Main {
    private final static Logger log = LoggerFactory.getLogger(Main.class.getName());

    public static void main(String[] args) {
        log.info("Minesweeper application started!");
        Application app = new Application();
        app.start();

    }
}
