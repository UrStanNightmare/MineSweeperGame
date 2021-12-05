package ru.ateam.minesweeper.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ateam.minesweeper.enums.GameType;
import ru.ateam.minesweeper.enums.UserStatus;
import ru.ateam.minesweeper.model.MineFieldModel;
import ru.ateam.minesweeper.utils.resultsdata.*;

import java.io.*;

public class SimpleDatabaseOperator implements DefaultDatabaseOperator {
    private final static Logger log = LoggerFactory.getLogger(SimpleDatabaseOperator.class.getName());

    private final static String databaseDirPath = "db/";
    private final static String databaseName = "msdb.json";
    private final static File databaseFile = new File(databaseDirPath + databaseName);

    private PlayerResults playerResults;

    private final boolean needsRecreation;

    private final boolean isAvailable;

    private final MineFieldModel model;

    private final Gson gson;


    public SimpleDatabaseOperator(MineFieldModel model) {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(PlayerResults.class, new CustomPlayerResultSerializer())
                .registerTypeAdapter(ResultsByGameType.class, new CustomPlayerResultByGameTypeSerializer())
                .create();



        this.model = model;
        DirectoryBuilder directoryBuilder = new DirectoryBuilder(databaseFile);
        boolean directoryAvailable = directoryBuilder.buildAndVerify();
        if (!directoryAvailable) {
            this.needsRecreation = false;
            this.isAvailable = false;

        } else {
            this.isAvailable = true;

            this.needsRecreation = directoryBuilder.isFileCreated();
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
            this.playerResults = new PlayerResults();
            this.saveDataToFile(databaseFile);
        }

        this.playerResults = this.readDataFromFile(databaseFile);

    }

    private void saveDataToFile(File saveFile) {
        String saveString = gson.toJson(this.playerResults);
        try (FileWriter file = new FileWriter(saveFile)) {
            file.write(saveString);
        } catch (IOException e) {
            log.error("Can't write player data to file. {}", e.getMessage(), e);
        }
    }

    private PlayerResults readDataFromFile(File readFile) {
        try (JsonReader reader = new JsonReader(new FileReader(readFile))) {
            PlayerResults results = new GsonBuilder()
                    .registerTypeAdapter(ResultsByGameType.class, new CustomPlayerResultByGameTypeDeserializer())
                    .registerTypeAdapter(PlayerResults.class, new CustomPlayerResultDeserializer())
                    .create()
                    .fromJson(reader, PlayerResults.class);

            return results;

        } catch (FileNotFoundException e) {
            log.error("Can't read db file! Created empty one {}", e.getMessage());
            return new PlayerResults();
        } catch (IOException e) {
            log.error("{}. Created empty one!", e.getMessage(), e);
            return new PlayerResults();
        }
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
        this.playerResults.addResult(type, username, time);

        this.saveDataToFile(databaseFile);

        log.info("Added data to db: {}/{}:{}", type, username, time);
    }

    /**
     * Возвращает массив результатов игроков по заданному игровому режиму
     *
     * @param type Игровой режим
     * @return Массив результатов по игровому режиму
     */
    @Override
    public ResultsByGameType getRecords(GameType type) {

        log.info("Returned {} record data ", type);
        return this.playerResults.getResultsByType(type);
    }

    /**
     * Записывает в json файл имя текущего пользователя
     *
     * @param username Имя пользователя
     */
    @Override
    public void changeCurrentUsername(String username) {
        this.playerResults.setLocalUsername(username);

        this.saveDataToFile(databaseFile);
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

        return this.playerResults.getLocalUsername();
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
    private UserStatus isUserExistsAndResultIsBetter(ResultsByGameType data, String currentUsername, int time) {
        log.info("Checking if player data needs to be written to database");

        if (data.isUserExists(currentUsername)) {
            if (data.isResultBetter(currentUsername, time)) {
                return UserStatus.USER_EXISTS_AND_RESULT_IS_BETTER;
            }
            return UserStatus.USER_EXISTS;
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
     * @see #isUserExistsAndResultIsBetter(ResultsByGameType, String, int)
     */
    @Override
    public UserStatus getUserStatus(GameType gameType, String localName, int time) {
        ResultsByGameType data = this.getRecords(gameType);
        UserStatus status = this.isUserExistsAndResultIsBetter(data, localName, time);

        return status;
    }

    /**
     * Возвращает результаты всех игроков во всех режимах (MAX_RECORDS_COUNT штук для каждого режима) как объект RecordsData
     *
     * @return Объект RecordsData с данными.
     */
    @Override
    @Deprecated
    public PlayerResults getRecordsAsData() {
        return this.playerResults;
    }


}
