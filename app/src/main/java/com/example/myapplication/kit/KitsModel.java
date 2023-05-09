package com.example.myapplication.kit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KitsModel {
    public static List<String> groupList;
    public static List<String> childList;
    public static Map<String, List<String>> medCollection;
    public static boolean initialized = false;


    public static void initialize() {
        if (!initialized) {
            groupList = new ArrayList<>();
            groupList.add("Экстренная помощь");

            String[] defaultModels = {"бинты", "пластыри"};
            medCollection = new HashMap<String, List<String>>();
            for (String group : groupList) {
                if (group.equals("Экстренная помощь")) {
                    loadChild(defaultModels);
                }
                medCollection.put(group, childList);
            }
            initialized = true;
        }
    }

    private static void loadChild(String[] medModels) {
        childList = new ArrayList<>();
        for (String model : medModels) {
            childList.add(model);
        }
    }
}
