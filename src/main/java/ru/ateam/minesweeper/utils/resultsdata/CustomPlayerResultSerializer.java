package ru.ateam.minesweeper.utils.resultsdata;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CustomPlayerResultSerializer implements JsonSerializer<PlayerResults> {
    private final static String GAME_TYPE_BASED_RESULTS = "Game type based results";
    private final static String CURRENT_PLAYER_NAME_STRING = "Current user";

    @Override
    public JsonElement serialize(PlayerResults src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject allData = new JsonObject();

        JsonObject playerData = new JsonObject();


        ArrayList<ResultsByGameType> results = src.getResults();

        for (ResultsByGameType res : results) {
            playerData.add(res.getGameType().toString(), context.serialize(res));
        }

        allData.add(GAME_TYPE_BASED_RESULTS, playerData);

        String localUsername = src.getLocalUsername();

        if (localUsername == null) {
            localUsername = "";
        }

        allData.addProperty(CURRENT_PLAYER_NAME_STRING, localUsername);

        return allData;
    }
}
