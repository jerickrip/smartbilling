package com.ni2.maskatel.smartbilling.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

public class AppUtils {

    private static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
        List<T> list = new ArrayList<>(c);
        java.util.Collections.sort(list);
        return list;
    }

    private static void printJSONObject(JSONObject jsonObject) {
        // If we need to dispaly in alphabetical order use:
        for (Object key : asSortedList(jsonObject.keySet())) {
            //for (Object key : jsonObject.keySet()) {
            if (null == jsonObject.get(key))
                continue;
            if (jsonObject.get(key) instanceof JSONObject) {
                System.out.println(key + ": {JSONObject}");
                continue;
            }
            if (jsonObject.get(key) instanceof JSONArray) {
                System.out.println(key + ": [JSONArray]");
                continue;
            }
            System.out.println(key + "-::-" + jsonObject.get(key));
        }
    }

    public static void extractJSONObject(JSONObject jsonObject){
        System.out.println("===================");
        System.out.println("Object data:");

        if(null == jsonObject)
            return;
        AppUtils.printJSONObject(jsonObject);
        System.out.println("===================");
    }

    public static void storeJSONObject(JSONObject jsonObject){
        if(null == jsonObject)
            return;
        AppUtils.printJSONObject(jsonObject);
    }

    public static void printTimeDiff(Date startTime){
        Long diff = (new Date()).getTime() - startTime.getTime();
        System.out.println("It took " + diff/1000 + " secs.");

    }

    public static String login(Properties properties) {
        String token = null;

        // Let's get the parameter need for login.
        Map<String, String> params = new HashMap<String, String>();
        params.put("key", properties.getProperty("apikey"));
        params.put("username", properties.getProperty("username"));
        params.put("password", properties.getProperty("password"));

        try {
            JSONObject response = RESTUtils.httpPOST(properties.getProperty("baseURL") + "/login", params);
            token = (String) response.get("token");
        } catch (Exception e) {
            System.out.println("Uncaught exception.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        System.out.println("===================");
        if (null == token)
            System.out.println("Login failed - check values in 'config.properties' file.");
        else
            System.out.println("Login successful.");
        System.out.println("===================");

        // Return token
        return token;
    }
}
