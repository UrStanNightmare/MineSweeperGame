package ru.ateam.minesweeper.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ateam.minesweeper.enums.ButtonType;
import ru.ateam.minesweeper.enums.GameImage;
import ru.ateam.minesweeper.enums.GameType;
import ru.ateam.minesweeper.enums.UserStatus;
import ru.ateam.minesweeper.listeners.ModelAudioListener;
import ru.ateam.minesweeper.listeners.TimerListener;
import ru.ateam.minesweeper.utils.DefaultDatabaseOperator;
import ru.ateam.minesweeper.utils.DefaultTimer;
import ru.ateam.minesweeper.utils.SimpleDatabaseOperator;
import ru.ateam.minesweeper.utils.resultsdata.PlayerResults;
import ru.ateam.minesweeper.view.MinesweeperView;

import java.util.Map;

public class DefaultMineFieldModel implements MineFieldModel, TimerListener {
    private final static Logger log = LoggerFactory.getLogger(DefaultMineFieldModel.class.getName());

    private GameType gameType = GameType.NOVICE;

    private final MineFieldData mineFieldData = new MineFieldData(this);

    private MinesweeperView view;
    private DefaultTimer timer;
    private boolean isDbAvailable;
    private boolean firstRunCompleted;
    private boolean nameNeedsToBeSpecified;

    private boolean firstClickPerformed;
    private boolean isLocked;
    private boolean isWin;

    private String localUsername;

    private final DefaultDatabaseOperator db;
    private int localTime;

    private ModelAudioListener audioListener;

    public DefaultMineFieldModel() {
        this.firstClickPerformed = false;
        this.mineFieldData.prepareData(gameType);
        this.db = new SimpleDatabaseOperator(this);
        this.db.checkAndCreateDocumentsIfNeeded();
        if (isDbAvailable) {
            this.updateLocalUsername(this.db.getStoredUsername());
        }
    }

    private void updateLocalUsername(String storedUsername) {
        if (storedUsername == null || storedUsername.isBlank()) {
            if (this.firstRunCompleted) {
                this.localUsername = this.generateDefaultName();
            } else {
                this.firstRunCompleted = true;
                this.nameNeedsToBeSpecified = true;
            }

        } else {
            this.localUsername = storedUsername;
            this.db.changeCurrentUsername(storedUsername);

        }
        if (view != null) {
            this.view.updateUsernameLabel(this.localUsername);
        }
    }

    @Override
    public void sendCellChangeCommand(int x, int y, GameImage type) {
        this.view.onChangeCellTypeCommand(x, y, type);
    }

    @Override
    public void sendMineCounterUpdateCommand(int minesCount) {
        if (this.view != null) {
            this.view.onMineCounterChanged(minesCount);
        }

    }

    @Override
    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    @Override
    public void registerView(MinesweeperView view) {
        this.view = view;
        if (this.nameNeedsToBeSpecified) {
            this.nameNeedsToBeSpecified = false;
            this.view.showChangeUsernameWindow();
        }
        this.view.updateUsernameLabel(this.localUsername);
    }

    @Override
    public void onStartNewGame() {
        this.view.updateGameField(this.gameType);
        this.firstClickPerformed = false;
        this.mineFieldData.prepareData(this.gameType);

    }

    @Override
    public void registerTimer(DefaultTimer timer) {
        this.timer = timer;
    }

    @Override
    public void processCellClick(int x, int y, ButtonType buttonType) {
        if (!this.checkIfLocked() && !this.isWin) {
            GameImage cellType = this.mineFieldData.getCellTypeCommand(x, y);
            switch (buttonType) {
                case RIGHT_BUTTON -> {
                    log.info("Right click at {}:{}", x, y);
                    switch (cellType) {
                        case CLOSED -> {
                            this.mineFieldData.changeCellType(x, y, GameImage.MARKED);
                            if (this.firstClickPerformed) {
                                this.isWin = this.mineFieldData.checkIfWin();

                                if (isWin) {
                                    this.timer.stopTimer();
                                    this.firstClickPerformed = false;
                                    log.info("VICTORY!");

                                    this.sendWinMessage();

                                }
                            }
                        }
                        case MARKED -> this.mineFieldData.changeCellType(x, y, GameImage.CLOSED);
                    }
                }
                case LEFT_BUTTON -> {
                    log.info("Left click at {}:{}", x, y);
                    if (cellType == GameImage.CLOSED) {
                        if (!firstClickPerformed) {
                            this.timer.startTimer();
                            this.mineFieldData.generateBombs(x, y);
                            this.firstClickPerformed = true;
                        }
                        boolean blowUp = this.mineFieldData.checkIfCellIsMine(x, y);
                        if (blowUp) {
                            this.firstClickPerformed = false;
                            this.timer.stopTimer();
                            this.mineFieldData.changeCellType(x, y, GameImage.BOMB_ICON);
                            this.view.openLoseWindow();
                            if (this.audioListener != null) {
                                this.audioListener.play(true);
                            }
                        }
                    }
                }
                case MIDDLE_BUTTON -> {
                    log.info("Middle click at {}:{}", x, y);
                    if (firstClickPerformed) {
                        if (this.mineFieldData.getCellTypeCommand(x, y) != GameImage.CLOSED) {
                            boolean hasFlagsNeededNearby = this.mineFieldData.checkIfFlagsNeededPlaced(x, y);
                            if (hasFlagsNeededNearby) {
                                boolean blownUp = this.mineFieldData.checkNearMinesFromRevealedCell(x, y);
                                if (blownUp) {
                                    this.firstClickPerformed = false;
                                    this.timer.stopTimer();
                                    this.view.openLoseWindow();
                                    if (this.audioListener != null) {
                                        this.audioListener.play(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void sendWinMessage() {
        if (this.isDbAvailable) {
            UserStatus status = db.getUserStatus(this.gameType, this.localUsername, this.localTime);

            switch (status) {
                case USER_EXISTS_AND_RESULT_IS_BETTER -> log.info("User already exists. New record! Will be rewritten to database.");
                case USER_NOT_EXISTS -> log.info("User not exists. New record then! Will be written to database.");
                case USER_EXISTS -> log.info("User exists but the result is not better. Will be ignored");
            }

            if (status != UserStatus.USER_EXISTS) {
                this.db.placeRecord(this.gameType, this.localUsername, this.localTime);
            }

            this.view.openWinWindow(this.localTime, status, this.localUsername);
        } else {
            this.view.openWinWindow(this.localTime, UserStatus.USER_NOT_EXISTS, this.localUsername);
        }
    }

    @Override
    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    @Override
    public void isUsingDb(boolean b) {
        this.isDbAvailable = b;
    }

    @Override
    public void usernameChanged(String name) {
        this.updateLocalUsername(name);
    }

    @Override
    public void onHighScoreCommand() {
        PlayerResults data = this.db.getRecordsAsData();

        this.view.openHighScoreWindow(data);
    }

    @Override
    public void registerAudioListener(ModelAudioListener listener) {
        this.audioListener = listener;
    }

    @Override
    public void closeResources() {
        if (this.audioListener != null) {
            this.audioListener.close();
        }
    }

    public boolean checkIfLocked() {
        return this.isLocked;
    }

    private String getComputerName() {
        Map<String, String> env = System.getenv();
        if (env.containsKey("COMPUTERNAME"))
            return env.get("COMPUTERNAME");
        else if (env.containsKey("HOSTNAME"))
            return env.get("HOSTNAME");
        else
            return "U";
    }

    private String generateDefaultName() {
        String username = "Gst on " + getComputerName();
        return username;
    }

    @Override
    public void onTimeChanged(int time) {
        this.localTime = time;
        this.view.onTimeChanged(this.localTime);
    }
}
