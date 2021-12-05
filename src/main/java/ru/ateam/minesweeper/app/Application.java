package ru.ateam.minesweeper.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ateam.minesweeper.controller.DefaultMinesweeperGameController;
import ru.ateam.minesweeper.listeners.TimerListener;
import ru.ateam.minesweeper.model.DefaultMineFieldModel;
import ru.ateam.minesweeper.utils.SimpleMusicPlayer;
import ru.ateam.minesweeper.utils.SimpleTimer;
import ru.ateam.minesweeper.view.SwingMinesweeperView;

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

        SimpleMusicPlayer boomPlayer = null;


        boomPlayer = new SimpleMusicPlayer(boomMusicName, false);
        model.registerAudioListener(boomPlayer);

        //File boomFile = new File(boomFilePath);


        view.show();
    }
}
