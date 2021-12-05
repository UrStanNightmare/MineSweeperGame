package ru.ateam.minesweeper.listeners;

import ru.ateam.minesweeper.enums.ButtonType;

public interface CellEventListener {
    void onMouseClick(int x, int y, ButtonType buttonType);
}
