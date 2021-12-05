package ru.ateam.minesweeper.view;

import ru.ateam.minesweeper.enums.GameImage;
import ru.ateam.minesweeper.enums.GameType;
import ru.ateam.minesweeper.enums.UserStatus;
import ru.ateam.minesweeper.utils.resultsdata.PlayerResults;

public interface MinesweeperView {
    void onTimeChanged(int time);

    void updateUsernameLabel(String username);

    void onMineCounterChanged(int mineCounter);

    void onChangeCellTypeCommand(int x, int y, GameImage gameImage);

    void openWinWindow(int time, UserStatus status, String name);

    void openLoseWindow();

    void updateGameField(GameType gameType);

    void showChangeUsernameWindow();

    void openHighScoreWindow(PlayerResults data);
}
