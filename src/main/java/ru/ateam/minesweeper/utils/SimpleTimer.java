package ru.ateam.minesweeper.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ateam.minesweeper.listeners.TimerListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SimpleTimer implements DefaultTimer {
    private final static Logger log = LoggerFactory.getLogger(SimpleTimer.class.getName());

    private int time;
    private Timer timer;
    private ArrayList<TimerListener> timeListeners;

    public SimpleTimer() {
        this.time = 0;
    }

    @Override
    public void registerTimeListeners(ArrayList<TimerListener> listeners) {
        this.timeListeners = listeners;
    }

    @Override
    public void startTimer() {
        log.info("Timer started!");

        this.time = 0;
        this.notifyListeners(this.time);

        timer = new Timer();
        this.timer.schedule(new SimpleTimerTask(), 0, 1000);
    }

    @Override
    public void stopTimer() {
        log.info("Timer stopped!");

        this.timer.cancel();
        this.notifyListeners(this.time);
    }

    @Override
    public void notifyListeners(int time) {
        for (TimerListener tl : timeListeners) {
            tl.onTimeChanged(time);
        }
    }

    class SimpleTimerTask extends TimerTask {
        @Override
        public void run() {
            time++;
            notifyListeners(time);
        }
    }
}
