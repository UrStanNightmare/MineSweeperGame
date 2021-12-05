package ru.ateam.minesweeper.controller;

import ru.ateam.minesweeper.listeners.CellEventListener;
import ru.ateam.minesweeper.listeners.GameTypeListener;

public interface MinesweeperGameController extends CellEventListener, GameTypeListener {

    void onStartNewGameAction();

    void onUserSpecifiedName(String name);

    void onShowHighScoresAction();

    void onCloseEvent();
}
