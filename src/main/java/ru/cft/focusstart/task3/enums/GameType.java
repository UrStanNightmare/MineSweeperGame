package ru.cft.focusstart.task3.enums;

public enum GameType {
    NOVICE(10, 9, 9),
    MEDIUM(40, 16, 16),
    EXPERT(99, 16, 30);

    private final int minesCount;
    private final int width;
    private final int height;

    GameType(int minesCount, int width, int height) {
        this.minesCount = minesCount;
        this.width = width;
        this.height = height;
    }

    public synchronized int getMinesCount() {
        return this.minesCount;
    }

    public synchronized int getHeight() {
        return height;
    }

    public synchronized int getWidth() {
        return width;
    }
}
