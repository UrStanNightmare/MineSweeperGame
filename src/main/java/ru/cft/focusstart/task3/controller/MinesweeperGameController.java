package ru.cft.focusstart.task3.controller;

import ru.cft.focusstart.task3.listeners.CellEventListener;
import ru.cft.focusstart.task3.listeners.GameTypeListener;

public interface MinesweeperGameController extends CellEventListener, GameTypeListener {

    void onStartNewGameAction();

    void onUserSpecifiedName(String name);

    void onShowHighScoresAction();

    void onCloseEvent();
}
