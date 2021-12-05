package ru.ateam.minesweeper.utils.resultsdata;

import com.google.gson.annotations.SerializedName;
import ru.ateam.minesweeper.enums.GameType;

import java.util.ArrayList;

public class PlayerResults {
    private final static int maxResultLists = GameType.values().length;

    @SerializedName("Game type based results")
    private final ArrayList<ResultsByGameType> results;

    @SerializedName("Current user")
    private String localUsername;


    public PlayerResults() {
        this.results = new ArrayList<>(maxResultLists);

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
}
