package ru.ateam.minesweeper.utils.resultsdata;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;

public class CustomPlayerResultByGameTypeSerializer implements JsonSerializer<ResultsByGameType> {
    private final static String USERNAME_STRING = "name";
    private final static String TIME_STRING = "time";

    @Override
    public JsonElement serialize(ResultsByGameType src, Type typeOfSrc, JsonSerializationContext context) {

        JsonArray results = new JsonArray();

        Map<String, Integer> resultsMap = src.getResultData();

        resultsMap.entrySet()
                .stream()
                .forEach(stringIntegerEntry -> {
                    JsonObject playerObject = new JsonObject();

                    playerObject.addProperty(USERNAME_STRING, stringIntegerEntry.getKey());
                    playerObject.addProperty(TIME_STRING, stringIntegerEntry.getValue());

                    results.add(playerObject);

                });


        return results;
    }
}
