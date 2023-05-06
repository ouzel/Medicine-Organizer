package com.example.myapplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KitsModel {
    public static List<String> groupList;
    public static List<String> childList;
    public static Map<String, List<String>> mobileCollection;
    public static boolean initialized = false;


    public static void initialize() {
        if (!initialized) {
            groupList = new ArrayList<>();
            groupList.add("Kit1");
            groupList.add("Kit2");

            String[] samsungModels = {"med11", "med12"};
            String[] googleModels = {"med21", "med22"};
            mobileCollection = new HashMap<String, List<String>>();
            for (String group : groupList) {
                if (group.equals("Kit1")) {
                    loadChild(samsungModels);
                } else if (group.equals("Kit2"))
                    loadChild(googleModels);
                mobileCollection.put(group, childList);
            }
            initialized = true;
        }
    }

    private static void loadChild(String[] mobileModels) {
        childList = new ArrayList<>();
        for (String model : mobileModels) {
            childList.add(model);
        }
    }
}
