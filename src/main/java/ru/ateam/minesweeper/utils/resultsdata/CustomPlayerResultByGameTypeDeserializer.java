package ru.ateam.minesweeper.utils.resultsdata;

import com.google.gson.*;
import ru.ateam.minesweeper.enums.GameType;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class CustomPlayerResultByGameTypeDeserializer implements JsonDeserializer<ResultsByGameType> {
    @Override
    public ResultsByGameType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        String type = object.get("gameType").getAsString();

        JsonArray dataArray = object.getAsJsonArray("data");

        ResultsByGameType results = new ResultsByGameType(GameType.valueOf(type));

        Map<String, Integer> resultData = new HashMap<>();

        for (JsonElement o : dataArray) {
            JsonObject playerData = o.getAsJsonObject();
            String name = playerData.get("name").getAsString();
            int time = playerData.get("time").getAsInt();

            resultData.put(name, time);
        }

        results.setResultData(resultData);

        return results;
    }
}
