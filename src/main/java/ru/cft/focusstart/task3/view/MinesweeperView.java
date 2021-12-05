package ru.cft.focusstart.task3.view;

import ru.cft.focusstart.task3.enums.GameImage;
import ru.cft.focusstart.task3.enums.GameType;
import ru.cft.focusstart.task3.enums.UserStatus;

import java.util.ArrayList;

public interface MinesweeperView {
    void onTimeChanged(int time);

    void updateUsernameLabel(String username);

    void onMineCounterChanged(int mineCounter);

    void onChangeCellTypeCommand(int x, int y, GameImage gameImage);

    void openWinWindow(int time, UserStatus status, String name);

    void openLoseWindow();

    void updateGameField(GameType gameType);

    void showChangeUsernameWindow();

    void openHighScoreWindow(ArrayList<ArrayList<String>> nameList, ArrayList<ArrayList<Integer>> timeList);
}
