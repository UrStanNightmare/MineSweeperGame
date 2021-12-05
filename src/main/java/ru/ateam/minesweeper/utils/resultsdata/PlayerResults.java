package ru.ateam.minesweeper.utils.resultsdata;

import ru.ateam.minesweeper.enums.GameType;

import java.util.ArrayList;

public class PlayerResults {
    private final static int maxResultLists = GameType.values().length;

    private ArrayList<ResultsByGameType> results;

    private String localUsername;


    public PlayerResults() {
        this.results = new ArrayList<>(maxResultLists);
        this.localUsername = null;

        for (GameType gameType : GameType.values()) {
            this.results.add(new ResultsByGameType(gameType));
        }
    }

    public void addResult(GameType gameType, String username, int time) {
        ResultsByGameType results = this.findResultsListByGameType(gameType);
        if (results != null) {
            results.addResult(username, time);
        }
    }

    private ResultsByGameType findResultsListByGameType(GameType gameType) {
        for (ResultsByGameType r : this.results) {
            if (r.getGameType() == gameType) {
                return r;
            }
        }
        return null;
    }

    public String getLocalUsername() {
        return localUsername;
    }

    public void setLocalUsername(String localUsername) {
        this.localUsername = localUsername;
    }

    public ResultsByGameType getResultsByType(GameType gameType) {
        return this.findResultsListByGameType(gameType);
    }

    public ArrayList<ResultsByGameType> getResults() {
        return results;
    }

    public void setResultsList(ArrayList<ResultsByGameType> resultsList) {
        this.results = resultsList;
    }
}
