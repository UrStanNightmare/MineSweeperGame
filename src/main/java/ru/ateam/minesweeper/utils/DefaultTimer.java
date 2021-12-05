package ru.ateam.minesweeper.utils;

import ru.ateam.minesweeper.listeners.TimerListener;

import java.util.ArrayList;

public interface DefaultTimer {
    void registerTimeListeners(ArrayList<TimerListener> listeners);

    void startTimer();

    void stopTimer();

    void notifyListeners(int time);
}
