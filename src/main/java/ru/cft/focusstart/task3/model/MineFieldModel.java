package ru.cft.focusstart.task3.model;

import ru.cft.focusstart.task3.enums.ButtonType;
import ru.cft.focusstart.task3.enums.GameImage;
import ru.cft.focusstart.task3.enums.GameType;
import ru.cft.focusstart.task3.listeners.ModelAudioListener;
import ru.cft.focusstart.task3.utils.DefaultTimer;
import ru.cft.focusstart.task3.view.MinesweeperView;

public interface MineFieldModel {
    void registerView(MinesweeperView view);

    void onStartNewGame();

    void registerTimer(DefaultTimer timer);

    void processCellClick(int x, int y, ButtonType buttonType);

    void setGameType(GameType gameType);

    void isUsingDb(boolean b);

    void usernameChanged(String name);

    void onHighScoreCommand();

    void registerAudioListener(ModelAudioListener listener);

    void closeResources();

    void sendCellChangeCommand(int x, int y, GameImage type);

    void sendMineCounterUpdateCommand(int minesCount);

    void setLocked(boolean isLocked);
}
