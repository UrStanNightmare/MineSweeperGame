package ru.cft.focusstart.task3.utils;

import ru.cft.focusstart.task3.listeners.TimerListener;

import java.util.ArrayList;

public interface DefaultTimer {
    void registerTimeListeners(ArrayList<TimerListener> listeners);

    void startTimer();

    void stopTimer();

    void notifyListeners(int time);
}
