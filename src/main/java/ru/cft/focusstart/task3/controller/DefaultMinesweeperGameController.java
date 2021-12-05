package ru.cft.focusstart.task3.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cft.focusstart.task3.enums.ButtonType;
import ru.cft.focusstart.task3.enums.GameType;
import ru.cft.focusstart.task3.listeners.CellEventListener;
import ru.cft.focusstart.task3.listeners.GameTypeListener;
import ru.cft.focusstart.task3.model.MineFieldModel;

public class DefaultMinesweeperGameController implements CellEventListener, GameTypeListener, MinesweeperGameController {
    private final static Logger log = LoggerFactory.getLogger(DefaultMinesweeperGameController.class.getName());
    private final MineFieldModel model;

    public DefaultMinesweeperGameController(MineFieldModel model) {
        this.model = model;
    }


    @Override
    public void onMouseClick(int x, int y, ButtonType buttonType) {
        this.model.processCellClick(x, y, buttonType);

    }

    @Override
    public void onGameTypeChanged(GameType gameType) {
        this.model.setGameType(gameType);
        this.model.onStartNewGame();
    }

    @Override
    public void onStartNewGameAction() {
        this.model.onStartNewGame();
    }

    @Override
    public void onUserSpecifiedName(String name) {
        this.model.usernameChanged(name);
    }

    @Override
    public void onShowHighScoresAction() {
        this.model.onHighScoreCommand();
    }

    @Override
    public void onCloseEvent() {
        this.model.closeResources();
    }
}
