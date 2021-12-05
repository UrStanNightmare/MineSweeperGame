package ru.ateam.minesweeper.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ateam.minesweeper.controller.MinesweeperGameController;
import ru.ateam.minesweeper.enums.GameImage;
import ru.ateam.minesweeper.enums.GameType;
import ru.ateam.minesweeper.enums.UserStatus;

import java.util.ArrayList;

public class SwingMinesweeperView implements MinesweeperView {
    private final static Logger log = LoggerFactory.getLogger(SwingMinesweeperView.class.getName());

    private final MainWindow mainWindow = new MainWindow();
    private final AboutWindow aboutWindow = new AboutWindow(this.mainWindow);
    private final PlayerControlsWindow controlsWindow = new PlayerControlsWindow(this.mainWindow);
    private final FieldSettingsWindow fieldSettingsWindow = new FieldSettingsWindow(this.mainWindow);
    private final HighScoresWindow highScoresWindow = new HighScoresWindow(this.mainWindow);
    private final SetUsernameWindow setUsernameWindow = new SetUsernameWindow(this.mainWindow);
    private final WinWindow winWindow = new WinWindow(this.mainWindow);
    private final LoseWindow loseWindow = new LoseWindow(this.mainWindow);

    private final MinesweeperGameController controller;

    public SwingMinesweeperView(MinesweeperGameController controller) {
        this.controller = controller;

        this.mainWindow.createGameField(GameType.NOVICE.getHeight(), GameType.NOVICE.getWidth());
        this.setUpMainWindowListeners();

        this.setUpSettingsWindowListeners();

        this.setUpLoseWindowListeners();

        this.setUpWinWindowListeners();
    }


    private void setUpMainWindowListeners() {
        this.mainWindow.setCellListener(this.controller);

        this.mainWindow.setExitMenuAction(e -> this.closeApplication());

        this.mainWindow.setNewGameMenuAction(e -> this.controller.onStartNewGameAction());

        this.mainWindow.setSettingsMenuAction(e -> {
            log.info("Field settings window opened.");
            this.fieldSettingsWindow.setVisible(true);
        });

        this.mainWindow.setHelpInfoMenuAction(e -> {
            log.info("Show controls window action");
            this.controlsWindow.setVisible(true);
        });

        this.mainWindow.setAboutMenuAction(e -> {
            log.info("Show about window action");
            this.aboutWindow.setVisible(true);
        });

        this.mainWindow.setHighScoresMenuAction(e -> this.controller.onShowHighScoresAction());

        this.mainWindow.setUsernameSettingsMenuAction(e -> {
            log.info("Username change window opened.");
            this.setUsernameWindow.setVisible(true);
            this.controller.onUserSpecifiedName(this.setUsernameWindow.getName());
        });
    }

    private void setUpSettingsWindowListeners() {
        this.fieldSettingsWindow.setGameTypeListener(this.controller);
    }

    private void setUpLoseWindowListeners() {
        this.loseWindow.setNewGameListener(e -> this.controller.onStartNewGameAction());
        this.loseWindow.setExitListener(e -> this.closeApplication());

        //Для начала новой игры, если юзер нажмет на крестик вместо кнопок.
        this.loseWindow.addWindowClosingListener(this.controller);
    }

    private void setUpWinWindowListeners() {
        this.winWindow.setNewGameListener(e -> this.controller.onStartNewGameAction());

        this.winWindow.setExitListener(e -> this.closeApplication());

        //Для начала новой игры, если юзер нажмет на крестик вместо кнопок.
        this.winWindow.addWindowClosingListener(this.controller);
    }

    private void closeApplication() {
        this.controller.onCloseEvent();

        this.mainWindow.dispose();
        log.info("Closing application. [Program exit]");
    }

    public void show() {
        this.mainWindow.setVisible(true);
    }

    @Override
    public void onTimeChanged(int time) {
        this.mainWindow.setTimerValue(time);
    }

    @Override
    public void updateUsernameLabel(String username) {
        this.mainWindow.updateUsernameLabel(username);
    }

    @Override
    public void onMineCounterChanged(int mineCounter) {
        this.mainWindow.setBombsCount(mineCounter);
    }

    @Override
    public void onChangeCellTypeCommand(int x, int y, GameImage gameImage) {
        this.mainWindow.setCellImage(x, y, gameImage);
    }

    @Override
    public void openWinWindow(int time, UserStatus status, String name) {
        this.winWindow.updateData(time, status, name);
        this.winWindow.setVisible(true);
    }

    @Override
    public void openLoseWindow() {
        this.loseWindow.setVisible(true);

    }

    @Override
    public void updateGameField(GameType gameType) {
        this.mainWindow.createGameField(gameType.getHeight(), gameType.getWidth());
    }

    @Override
    public void showChangeUsernameWindow() {
        this.setUsernameWindow.setVisible(true);
        this.controller.onUserSpecifiedName(this.setUsernameWindow.getName());
    }

    @Override
    public void openHighScoreWindow(ArrayList<ArrayList<String>> nameList, ArrayList<ArrayList<Integer>> timeList) {
        for (GameType type : GameType.values()) {
            if (type.ordinal() >= nameList.size()) {
                continue;
            }
            this.highScoresWindow.setRecordByType(type, nameList.get(type.ordinal()), timeList.get(type.ordinal()));
        }

        this.highScoresWindow.setVisible(true);

    }

}
