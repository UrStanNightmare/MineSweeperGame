package ru.ateam.minesweeper.model;

import ru.ateam.minesweeper.enums.ButtonType;
import ru.ateam.minesweeper.enums.GameImage;
import ru.ateam.minesweeper.enums.GameType;
import ru.ateam.minesweeper.listeners.ModelAudioListener;
import ru.ateam.minesweeper.utils.DefaultTimer;
import ru.ateam.minesweeper.view.MinesweeperView;

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
