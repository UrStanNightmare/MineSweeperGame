module minesweeper {
    requires org.slf4j;
    requires java.desktop;
    requires com.google.gson;

    exports ru.ateam.minesweeper;

    exports ru.ateam.minesweeper.enums to com.google.gson;
}