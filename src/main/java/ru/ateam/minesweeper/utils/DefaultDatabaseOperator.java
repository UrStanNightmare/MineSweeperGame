package ru.ateam.minesweeper.utils;

import org.json.simple.JSONArray;
import ru.ateam.minesweeper.enums.GameType;
import ru.ateam.minesweeper.enums.UserStatus;

public interface DefaultDatabaseOperator {
    void checkAndCreateDocumentsIfNeeded();

    void placeRecord(GameType type, String username, int time);

    JSONArray getRecords(GameType type);

    void changeCurrentUsername(String username);

    String getStoredUsername();

    UserStatus getUserStatus(GameType gameType, String localUsername, int localTime);

    RecordsData getRecordsAsData();
}
