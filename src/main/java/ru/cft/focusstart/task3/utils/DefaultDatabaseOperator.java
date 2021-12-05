package ru.cft.focusstart.task3.utils;

import org.json.simple.JSONArray;
import ru.cft.focusstart.task3.enums.GameType;
import ru.cft.focusstart.task3.enums.UserStatus;

public interface DefaultDatabaseOperator {
    void checkAndCreateDocumentsIfNeeded();

    void placeRecord(GameType type, String username, int time);

    JSONArray getRecords(GameType type);

    void changeCurrentUsername(String username);

    String getStoredUsername();

    UserStatus getUserStatus(GameType gameType, String localUsername, int localTime);

    RecordsData getRecordsAsData();
}
