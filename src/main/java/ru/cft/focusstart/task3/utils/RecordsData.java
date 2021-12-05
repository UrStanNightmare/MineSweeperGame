package ru.cft.focusstart.task3.utils;

import java.util.ArrayList;

/**
 * Класс для хранения данных в виде листа листов.
 * То есть для каждого игрового режима результаты хранятся в паре листов(nameList, timeList)
 * nameListsList[0] и timeListsList[0] соответствуют NOVICE и т.д.
 * Сделано для возможноти расширения количества игровых режимов.
 */
public class RecordsData {
    private final ArrayList<ArrayList<String>> nameListsList;
    private final ArrayList<ArrayList<Integer>> timeListsList;

    public RecordsData(ArrayList<ArrayList<String>> nameListsList, ArrayList<ArrayList<Integer>> timeListsList) {
        this.nameListsList = nameListsList;
        this.timeListsList = timeListsList;
    }

    public ArrayList<ArrayList<String>> getNameListsList() {
        return nameListsList;
    }

    public ArrayList<ArrayList<Integer>> getTimeListsList() {
        return timeListsList;
    }
}
