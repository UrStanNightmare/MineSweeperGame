package ru.cft.focusstart.task3.listeners;

import ru.cft.focusstart.task3.enums.ButtonType;

public interface CellEventListener {
    void onMouseClick(int x, int y, ButtonType buttonType);
}
