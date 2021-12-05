package ru.cft.focusstart.task3.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cft.focusstart.task3.controller.DefaultMinesweeperGameController;
import ru.cft.focusstart.task3.listeners.TimerListener;
import ru.cft.focusstart.task3.model.DefaultMineFieldModel;
import ru.cft.focusstart.task3.utils.SimpleMusicPlayer;
import ru.cft.focusstart.task3.utils.SimpleTimer;
import ru.cft.focusstart.task3.view.SwingMinesweeperView;

import java.io.File;
import java.util.ArrayList;

public class Application {
    private final static Logger log = LoggerFactory.getLogger(Application.class.getName());
    private final static String boomMusicName = "boom.wav";

    public void start() {
        DefaultMineFieldModel model = new DefaultMineFieldModel();
        DefaultMinesweeperGameController controller = new DefaultMinesweeperGameController(model);
        SwingMinesweeperView view = new SwingMinesweeperView(controller);

        model.registerView(view);

        SimpleTimer timer = new SimpleTimer();

        ArrayList<TimerListener> timerListeners = new ArrayList<>();
        timerListeners.add(model);
        timer.registerTimeListeners(timerListeners);

        model.registerTimer(timer);

        SimpleMusicPlayer boomPlayer = new SimpleMusicPlayer(new File(ClassLoader.getSystemResource(boomMusicName).getPath()), false);

        model.registerAudioListener(boomPlayer);

        view.show();
    }
}
