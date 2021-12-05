package ru.ateam.minesweeper.utils.resultsdata;

import ru.ateam.minesweeper.enums.GameType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ResultsByGameType {
    private final GameType gameType;

    private Map<String, Integer> resultData = new HashMap<>();

    public ResultsByGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void addResult(String name, int time) {
        this.resultData.put(name, time);
    }

    public void deleteResult(String name) {
        this.resultData.remove(name);
    }

    public ArrayList<Map.Entry<String, Integer>> getResultDataAsSortedmap() {
        return (ArrayList<Map.Entry<String, Integer>>) this.resultData.entrySet()
                .stream()
                .sorted((Comparator.comparing(Map.Entry::getValue)
                ))
                .collect(Collectors.toList());
    }

    public boolean isUserExists(String username) {
        return this.resultData.containsKey(username);
    }

    public boolean isResultBetter(String currentUsername, int time) {
        Integer localTime = this.resultData.get(currentUsername);
        if (localTime == null) {
            return true;
        }

        if (time < localTime) {
            return true;
        }
        return false;
    }
}
