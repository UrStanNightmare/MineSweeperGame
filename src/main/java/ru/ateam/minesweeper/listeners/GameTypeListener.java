package ru.ateam.minesweeper.listeners;

import ru.ateam.minesweeper.enums.GameType;

public interface GameTypeListener {
    void onGameTypeChanged(GameType gameType);
}
