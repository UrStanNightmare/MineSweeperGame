package ru.ateam.minesweeper.utils.resultsdata;

import com.google.gson.*;
import ru.ateam.minesweeper.enums.GameType;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CustomPlayerResultDeserializer implements JsonDeserializer<PlayerResults> {
    private final static String GAME_TYPE_BASED_RESULTS = "Game type based results";
    private final static String CURRENT_PLAYER_NAME_STRING = "Current user";

    @Override
    public PlayerResults deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        PlayerResults results = new PlayerResults();

        JsonObject dataObject = json.getAsJsonObject();

        String username = dataObject.get(CURRENT_PLAYER_NAME_STRING).getAsString();

        results.setLocalUsername(username);

        ArrayList<ResultsByGameType> resultsList = results.getResults();
        resultsList.clear();

        JsonObject gameTypeBasedResults = dataObject.getAsJsonObject(GAME_TYPE_BASED_RESULTS);

        for (GameType gameType : GameType.values()) {
            JsonArray dataByTypeArray = gameTypeBasedResults.getAsJsonArray(gameType.toString());

            JsonObject helper = new JsonObject();
            helper.addProperty("gameType", gameType.toString());
            helper.add("data", dataByTypeArray);

            resultsList.add(context.deserialize(helper, ResultsByGameType.class));
        }

        results.setResultsList(resultsList);

        return results;
    }
}
