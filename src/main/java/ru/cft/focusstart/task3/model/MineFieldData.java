package ru.cft.focusstart.task3.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cft.focusstart.task3.enums.GameImage;
import ru.cft.focusstart.task3.enums.GameType;

import java.util.Random;

public class MineFieldData {
    private final static Logger log = LoggerFactory.getLogger(MineFieldData.class.getName());

    private int maxMinesCount;
    private int flagsPlaced;
    private int width;
    private int height;
    private int mineCounter;

    private int[][] mines;
    private boolean[][] flags;
    private boolean[][] revealed;


    private int[][] minesNearbyCache;

    private final MineFieldModel model;


    public MineFieldData(MineFieldModel model) {
        this.model = model;
    }

    public int getMineCounter() {
        return mineCounter;
    }

    public int getMinesNearCellCount(int x, int y) {
        int minesNearCount = 0;

        for (int offsetX = -1; offsetX <= 1; offsetX++) {
            for (int offsetY = -1; offsetY <= 1; offsetY++) {
                if (outBounds(offsetX + x, offsetY + y)) continue;
                minesNearCount += mines[offsetY + y][offsetX + x];
            }
        }

        return minesNearCount;
    }

    private boolean outBounds(int x, int y) {
        return x < 0 || y < 0 || x >= this.width || y >= this.height;
    }

    private void revealCell(int x, int y) {
        if (outBounds(x, y)) return;
        if (revealed[y][x]) return;
        if (mines[y][x] != 0) return;
        revealed[y][x] = true;


        int minesNear = this.getMinesNearCellCount(x, y);
        this.minesNearbyCache[y][x] = minesNear;
        switch (minesNear) {
            case 0 -> this.model.sendCellChangeCommand(x, y, GameImage.EMPTY);
            case 1 -> this.model.sendCellChangeCommand(x, y, GameImage.NUM_1);
            case 2 -> this.model.sendCellChangeCommand(x, y, GameImage.NUM_2);
            case 3 -> this.model.sendCellChangeCommand(x, y, GameImage.NUM_3);
            case 4 -> this.model.sendCellChangeCommand(x, y, GameImage.NUM_4);
            case 5 -> this.model.sendCellChangeCommand(x, y, GameImage.NUM_5);
            case 6 -> this.model.sendCellChangeCommand(x, y, GameImage.NUM_6);
            case 7 -> this.model.sendCellChangeCommand(x, y, GameImage.NUM_7);
            case 8 -> this.model.sendCellChangeCommand(x, y, GameImage.NUM_8);
        }
        if (this.flags[y][x]) {
            this.flags[y][x] = false;
            this.flagsPlaced--;
            this.mineCounter = this.calculateMinesCounter();
            this.model.sendMineCounterUpdateCommand(this.mineCounter);
        }


        if (minesNear != 0) return;
        revealCell(x - 1, y - 1);
        revealCell(x - 1, y + 1);
        revealCell(x + 1, y - 1);
        revealCell(x + 1, y + 1);
        revealCell(x - 1, y);
        revealCell(x + 1, y);
        revealCell(x, y - 1);
        revealCell(x, y + 1);
    }

    private int calculateMinesCounter() {
        return this.maxMinesCount - this.flagsPlaced;
    }

    private int[][] generateMineArray(int maxMinesCount, int width, int height, int restrX, int restrY) {
        int[][] mineArray = new int[height][width];
        int minePlaced = 0;

        int min = 0;
        int maxX = width - 1;
        int maxY = height - 1;
        int diffX = maxX - min;
        int diffY = maxY - min;

        Random random = new Random();

        while (minePlaced < maxMinesCount) {
            int x = random.nextInt(diffX);
            int y = random.nextInt(diffY);

            //Проверяем попадаем ли мы в стартовую точку.
            if (x == restrX && y == restrY) {
                continue;
            }

            if (mineArray[y][x] == 1) {
                continue;
            } else {
                mineArray[y][x] = 1;
                minePlaced++;
                log.info("Placed mine[{}]", minePlaced - 1);
            }

        }
        this.model.sendMineCounterUpdateCommand(this.getMineCounter());
        return mineArray;
    }

    public boolean checkIfCellIsMine(int x, int y) {
        if (this.mines[y][x] != 0) {
            return true;
        } else {
            this.revealCell(x, y);
            return false;
        }
    }

    public GameImage getCellTypeCommand(int x, int y) {
        GameImage local = GameImage.CLOSED;
        if (this.revealed[y][x]) {
            local = GameImage.EMPTY;
        }
        if (this.flags[y][x]) {
            local = GameImage.MARKED;
        }
        return local;
    }

    public void changeCellType(int x, int y, GameImage type) {
        switch (type) {
            case MARKED -> {
                this.flagsPlaced++;
                this.flags[y][x] = true;

                this.model.sendCellChangeCommand(x, y, GameImage.MARKED);

                this.model.sendMineCounterUpdateCommand(this.calculateMinesCounter());
            }
            case CLOSED -> {
                this.flagsPlaced--;
                this.flags[y][x] = false;

                this.model.sendCellChangeCommand(x, y, GameImage.CLOSED);

                log.info("Flag removed from {}:{}", x, y);

                this.model.sendMineCounterUpdateCommand(this.calculateMinesCounter());
            }
            case BOMB_ICON -> {

                this.model.setLocked(true);
                this.model.sendCellChangeCommand(x, y, GameImage.BOMB_ICON);
                this.mines[y][x] = -1;

                for (int ly = 0; ly < this.height; ly++) {
                    for (int lx = 0; lx < this.width; lx++) {
                        if (flags[ly][lx]) {
                            if (mines[ly][lx] == 0) {
                                this.model.sendCellChangeCommand(lx, ly, GameImage.BOMB_CROSSED);
                                continue;
                            }
                            if (mines[ly][lx] != 0)
                                this.model.sendCellChangeCommand(lx, ly, GameImage.BOMB_FOUND);
                            continue;
                        }
                        if (mines[ly][lx] != 0 && mines[ly][lx] != -1) {
                            this.model.sendCellChangeCommand(lx, ly, GameImage.BOMB);
                            continue;
                        }
                    }
                }
                log.info("Blown up! Game over!");
            }
        }

    }

    public void generateBombs(int x, int y) {
        this.mines = generateMineArray(this.maxMinesCount, this.width, this.height, x, y);

        log.info("Minefield model created with size [{}x{}] and {} mines", this.width, this.height, this.maxMinesCount);
    }

    public boolean checkIfFlagsNeededPlaced(int x, int y) {
        int flagsNearbyPlaced = 0;

        for (int offsetX = -1; offsetX <= 1; offsetX++) {
            for (int offsetY = -1; offsetY <= 1; offsetY++) {
                if (outBounds(offsetX + x, offsetY + y)) continue;
                if (this.flags[offsetY + y][offsetX + x]) {
                    flagsNearbyPlaced++;
                }
            }
        }
        if (flagsNearbyPlaced == this.minesNearbyCache[y][x]) {
            return true;
        }
        return false;
    }

    private boolean checkNearbyCellsOnMinesAndBlowUp(int x, int y) {
        for (int offsetX = -1; offsetX <= 1; offsetX++) {
            for (int offsetY = -1; offsetY <= 1; offsetY++) {
                if (outBounds(offsetX + x, offsetY + y)) continue;
                if (this.mines[offsetY + y][offsetX + x] != 0 && !this.flags[offsetY + y][offsetX + x]) {
                    this.changeCellType(offsetX + x, offsetY + y, GameImage.BOMB_ICON);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkNearMinesFromRevealedCell(int x, int y) {
        boolean blownUp = this.checkNearbyCellsOnMinesAndBlowUp(x, y);
        revealCell(x - 1, y - 1);
        revealCell(x - 1, y + 1);
        revealCell(x + 1, y - 1);
        revealCell(x + 1, y + 1);
        revealCell(x - 1, y);
        revealCell(x + 1, y);
        revealCell(x, y - 1);
        revealCell(x, y + 1);
        return blownUp;
    }

    private boolean convertIntMineToBool(int x, int y) {
        if (this.mines != null) {
            if (this.mines[y][x] != 0) {
                return true;
            }
        }
        return false;
    }

    public boolean checkIfWin() {
        boolean missMatch = false;
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < width; x++) {
                if (this.convertIntMineToBool(x, y) != this.flags[y][x]) {
                    missMatch = true;
                    break;
                }
            }
        }
        if (missMatch) {
            return false;
        }

        return true;
    }

    public void prepareData(GameType type) {
        this.maxMinesCount = type.getMinesCount();
        this.width = type.getWidth();
        this.height = type.getHeight();
        this.flags = new boolean[this.height][this.width];
        this.revealed = new boolean[this.height][this.width];
        this.mineCounter = this.maxMinesCount;
        this.minesNearbyCache = new int[this.height][this.width];
        this.flagsPlaced = 0;
        this.model.setLocked(false);
        this.model.sendMineCounterUpdateCommand(this.mineCounter);

        log.info("New game data for game mode {} prepared", type);
    }
}
