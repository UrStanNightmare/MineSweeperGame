package ru.ateam.minesweeper.utils;

import ru.ateam.minesweeper.enums.GameType;
import ru.ateam.minesweeper.enums.UserStatus;
import ru.ateam.minesweeper.utils.resultsdata.PlayerResults;
import ru.ateam.minesweeper.utils.resultsdata.ResultsByGameType;

public interface DefaultDatabaseOperator {
    void checkAndCreateDocumentsIfNeeded();

    void placeRecord(GameType type, String username, int time);

    ResultsByGameType getRecords(GameType type);

    void changeCurrentUsername(String username);

    String getStoredUsername();

    UserStatus getUserStatus(GameType gameType, String localUsername, int localTime);

    PlayerResults getRecordsAsData();
}
