package ru.ateam.minesweeper.utils;

import org.json.simple.JSONObject;

import java.util.Comparator;

public class JsonUserDataComparator implements Comparator<JSONObject> {

    @Override
    public int compare(JSONObject o1, JSONObject o2) {
        System.out.println("comparing");
        String param = "time";
        Long data1 = (Long) o1.get(param);
        Long data2 = (Long) o2.get(param);

        if (data1 == data2) {
            return 0;
        } else {
            if (data1 > data2) {
                return 1;
            }
            return -1;

        }
    }
}
