package ru.cft.focusstart.task3.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cft.focusstart.task3.enums.GameType;
import ru.cft.focusstart.task3.enums.UserStatus;
import ru.cft.focusstart.task3.model.MineFieldModel;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimpleDatabaseOperator implements DefaultDatabaseOperator {
    private final static Logger log = LoggerFactory.getLogger(SimpleDatabaseOperator.class.getName());

    private final static int MAX_RECORDS_COUNT = 10;

    private final static String databaseDirPath = "db/";
    private final static String databaseFileName = "task3.json";
    private final static String currentUsernameKey = "current_user";
    private final static String usernameKey = "username";
    private final static String timeKey = "time";
    private final static File databaseFile = new File(databaseDirPath + databaseFileName);

    private final List<String> collectionNames = new ArrayList<>();

    private final boolean needsRecreation;

    private final boolean isAvailable;

    private final MineFieldModel model;

    public SimpleDatabaseOperator(MineFieldModel model) {
        this.model = model;
        DirectoryBuilder directoryBuilder = new DirectoryBuilder(databaseFile);
        boolean directoryAvailable = directoryBuilder.buildAndVerify();
        if (!directoryAvailable) {
            this.needsRecreation = false;
            this.isAvailable = false;

        } else {
            this.isAvailable = true;

            this.needsRecreation = directoryBuilder.isFileCreated();

            for (GameType type : GameType.values()) {
                this.collectionNames.add(type.toString());
            }
        }
    }

    /**
     * В случае если файл данных можно создать на диске, создаёт стандартный json файл для сохранения информации.
     */
    @Override
    public void checkAndCreateDocumentsIfNeeded() {
        if (this.isAvailable) {
            this.model.isUsingDb(true);
        } else {
            this.model.isUsingDb(false);
            return;
        }

        if (this.needsRecreation) {
            log.info("Trying to create database.");
            writeDataToFile(createDefaultDatabaseData());
        }

        JSONObject checkObject = readDataFromFile();
        boolean mismatch = false;
        for (String colName : collectionNames) {
            if (!checkObject.containsKey(colName)) {
                mismatch = true;
                break;
            }
        }
        if (!checkObject.containsKey(currentUsernameKey)) {
            mismatch = true;
        }


        if (mismatch) {
            log.info("Database is empty or damaged. Creating default database");
            writeDataToFile(createDefaultDatabaseData());
        }

    }

    private JSONObject createDefaultDatabaseData() {
        JSONObject data = new JSONObject();
        for (String colName : collectionNames) {
            JSONArray playerData = new JSONArray();
            data.put(colName, playerData);
        }
        data.put(currentUsernameKey, null);
        return data;
    }

    private void writeDataToFile(JSONObject object) {
        try (FileWriter file = new FileWriter(databaseFile)) {
            file.write(object.toJSONString());
        } catch (IOException e) {
            log.error("Can't write player data to file. {}", e.getMessage(), e);
        }
    }

    private JSONObject readDataFromFile() {
        JSONObject data = null;

        JSONParser parser = new JSONParser();

        try (Reader reader = new FileReader(databaseFile)) {
            data = (JSONObject) parser.parse(reader);
        } catch (FileNotFoundException e) {
            log.error("Can't find data file. {}", e.getMessage(), e);
        } catch (IOException e) {
            log.error("Can't read player data from file. {}", e.getMessage(), e);
        } catch (ParseException e) {
            log.error("Can't parse player data from file. {}", e.getMessage(), e);
        }

        return data;
    }

    /**
     * Записывает результат игрока в json файл.
     *
     * @param type     Режим игры
     * @param username Имя пользователя
     * @param time     Время победы
     */
    @Override
    public void placeRecord(GameType type, String username, int time) {
        JSONObject playerData = new JSONObject();
        playerData.put(usernameKey, username);
        playerData.put(timeKey, time);

        JSONObject data = readDataFromFile();
        JSONArray categoryArray = (JSONArray) data.get(type.toString());

        categoryArray.add(playerData);

        data.put(type.toString(), categoryArray);

        writeDataToFile(data);

        log.info("Added data to db: {}/{}:{}", type, username, time);
    }

    /**
     * Возвращает массив результатов игроков по заданному игровому режиму
     *
     * @param type Игровой режим
     * @return Массив результатов по игровому режиму
     */
    @Override
    public JSONArray getRecords(GameType type) {
        JSONObject data = readDataFromFile();

        JSONArray categoryArray = (JSONArray) data.get(type.toString());
        categoryArray.sort(new JsonUserDataComparator());

        log.info("Returned {} record data ", type);
        return categoryArray;
    }

    /**
     * Записывает в json файл имя текущего пользователя
     *
     * @param username Имя пользователя
     */
    @Override
    public void changeCurrentUsername(String username) {
        JSONObject data = readDataFromFile();
        data.put(currentUsernameKey, username);

        writeDataToFile(data);
        log.info("Username updated.");
    }

    /**
     * Возвращает имя записанного активного пользователя
     *
     * @return Имя пользователя
     */
    @Override
    public String getStoredUsername() {
        log.info("Username returned from database.");
        JSONObject data = readDataFromFile();
        String username = (String) data.get(currentUsernameKey);

        return username;
    }


    /**
     * Проверяет результат игрока в базе, и возвращает статус.
     * USER_EXISTS - пользователь есть в базе, но его результат хуже
     * USER_EXISTS_AND_RESULT_IS_BETTER - пользователь улучшил свой результат
     * USER_NOT_EXISTS - пользователя ещё нет в базе
     *
     * @param data            Данные по которым идёт проверка
     * @param currentUsername Пользователь по которому идёт проверка
     * @param time            Текущее время выигрыша пользователя.
     * @return Статус пользователя.
     */
    private UserStatus isUserExistsAndResultIsBetter(JSONArray data, String currentUsername, int time) {
        log.info("Checking if player data needs to be written to database");
        Iterator iter = data.iterator();
        while (iter.hasNext()) {
            JSONObject user = (JSONObject) iter.next();
            if (user.containsKey(usernameKey) && user.containsValue(currentUsername)) {
                long storedTime = (long) user.get(timeKey);
                if (storedTime > time) {
                    return UserStatus.USER_EXISTS_AND_RESULT_IS_BETTER;
                }
                return UserStatus.USER_EXISTS;
            }
        }
        return UserStatus.USER_NOT_EXISTS;
    }

    /**
     * Возвращает статус игрока. Обертка.
     *
     * @param gameType  Игровой режим.
     * @param localName Имя пользователя.
     * @param time      Время победы.
     * @return Статус игрока.
     * @see #isUserExistsAndResultIsBetter(JSONArray, String, int)
     */
    @Override
    public UserStatus getUserStatus(GameType gameType, String localName, int time) {
        JSONArray data = this.getRecords(gameType);
        UserStatus status = this.isUserExistsAndResultIsBetter(data, localName, time);

        return status;
    }

    /**
     * Возвращает результаты всех игроков во всех режимах (MAX_RECORDS_COUNT штук для каждого режима) как объект RecordsData
     *
     * @return Объект RecordsData с данными.
     * @see RecordsData
     */
    @Override
    public RecordsData getRecordsAsData() {
        //Ограничивается константой MAX_RECORDS_COUNT, чтобы не тянуть все результаты в программу. Не самый идеальный вариант.
        ArrayList<ArrayList<String>> namesListsList = new ArrayList<>(GameType.values().length);
        ArrayList<ArrayList<Integer>> timesListsList = new ArrayList<>(GameType.values().length);

        int gameTypeIter = 0;
        for (GameType type : GameType.values()) {
            JSONArray playerDataByType = this.getRecords(type);
            if (playerDataByType.size() > 0) {
                List<String> namesList = new ArrayList<>(playerDataByType.size());
                List<Integer> timeList = new ArrayList<>(playerDataByType.size());

                int currentRecords = 0;

                Iterator iter = playerDataByType.iterator();
                while (iter.hasNext()) {
                    if (currentRecords >= MAX_RECORDS_COUNT) {
                        break;
                    }
                    JSONObject playerData = (JSONObject) iter.next();
                    String name = (String) playerData.get("username");
                    Long time = (Long) playerData.get("time");
                    namesList.add(name);
                    timeList.add(Math.toIntExact(time));
                    currentRecords++;
                }
                namesListsList.add((ArrayList<String>) namesList);
                timesListsList.add((ArrayList<Integer>) timeList);
                gameTypeIter++;
            }

        }

        return new RecordsData(namesListsList, timesListsList);
    }


}
