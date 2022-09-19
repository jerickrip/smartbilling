package com.ni2.maskatel.smartbilling.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.opencsv.CSVWriter;

public class APIUtils {
    private static final Map<String, String> classURLs;
    private static final Map<String, String> classKeys;

    private static Map<String, JSONObject> customerMap;
    private static Map<String, JSONObject> custLocMap;
    private static Map<String, JSONObject> addressMap;
    private static Map<String, JSONObject> packageMap;
    private static Map<String, JSONObject> serviceMap;
    private static Map<String, JSONObject> subserviceMap;
    private static Map<String, JSONObject> miscchargeMap;

    static {
        Map<String, String> urls = new HashMap<>();
        urls.put("Customer", "/customer");
        urls.put("CustomerLocations", "/customer-location");
        urls.put("CustomerSubscriptions", "/customer-subscription");
        urls.put("CustomerProducts", "/customer-product");
        urls.put("CustomerAddress","/location");
        urls.put("CustomerSubCharges", "/customer-sub-charge");
        urls.put("CustomerMiscCharges", "/customer-misc-charge");
//        urls.put("CustomerContacts", "/customer-contact");
        classURLs = Collections.unmodifiableMap(urls);

        Map<String, String> keys = new HashMap<>();
        keys.put("Customer", "accnt_id");
        keys.put("CustomerLocations", "location_id");
        keys.put("CustomerSubscriptions", "accnt_service_id");
        keys.put("CustomerProducts", "accnt_product_id");
        keys.put("CustomerAddress","location_id");
        keys.put("CustomerSubCharges", "accnt_product_id");
        keys.put("CustomerMiscCharges", "accnt_product_id");
//        keys.put("CustomerContacts", "contact_id");
        classKeys = Collections.unmodifiableMap(keys);

        customerMap = new HashMap<>();
        custLocMap = new HashMap<>();
        addressMap = new HashMap<>();
        packageMap = new HashMap<>();
        serviceMap = new HashMap<>();
        subserviceMap = new HashMap<>();
        miscchargeMap = new HashMap<>();
    }

    public static Map<String,Map> getMaps() {
        Map<String, Map> dataMaps = new HashMap<>();
        dataMaps.put("Customer", customerMap);
        dataMaps.put("CustomerLocations", custLocMap);
        dataMaps.put("CustomerAddress", addressMap);
        dataMaps.put("CustomerSubscriptions", packageMap);
        dataMaps.put("CustomerProducts", serviceMap);
        dataMaps.put("CustomerSubCharges", subserviceMap);
        dataMaps.put("CustomerMiscCharges", miscchargeMap);
        return dataMaps;
    }

    public static JSONArray getAllForClass(String className, String baseURL, String token) {
        JSONArray objects = null;
        String url = baseURL + classURLs.get(className);
        try {
            objects = (JSONArray) RESTUtils.httpGET(url, token, null).get("data");
        } catch (Exception e) {
            System.out.println("Uncaught exception.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return objects;
    }

    public static JSONObject getObjectForClass(String className, String baseURL, String token, Object id) {
        JSONObject object = null;
        String url = baseURL + classURLs.get(className) + "/" + id;
        try {
            object = RESTUtils.httpGET(url, token, null);
        } catch (Exception e) {
            System.out.println("Uncaught exception.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return object;
    }

    public static void getAllObjectsDetailsForClass(String className, String baseURL, String token) {
        JSONArray objects;
        String url = baseURL + classURLs.get(className);

        // Paging control
        int i = 1;
        Map<String, String> params = new HashMap<>();
        params.put("page", String.valueOf(i));

        try {
            JSONObject response = RESTUtils.httpGET(url, token, params);
            objects = (JSONArray) response.get("data");
//            System.out.println("JSONobject "+response.get("data").toString());
            while ((boolean) response.get("hasMore")){
                params.put("page", String.valueOf(++i));
                response = RESTUtils.httpGET(url, token, params);
                objects.addAll((JSONArray) response.get("data"));
            }

            System.out.println("We have " + objects.size() + " " + className + ".");
            for (Object object : objects) {
                storeJSONObject((JSONObject) object, className);
            }

        } catch (Exception e) {
            System.out.println("Uncaught exception.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private static void storeJSONObject(JSONObject jsonObject, String className) {
        if (null == jsonObject)
            return;

        Map<String, JSONObject> classMap = null;
        switch (className) {
            case "Customer":
                classMap = customerMap;
                break;
            case "CustomerLocations":
                classMap = custLocMap;
                break;
            case "CustomerAddress":
                classMap = addressMap;
                break;
            case "CustomerSubscriptions":
                classMap = packageMap;
                break;
            case "CustomerProducts":
                classMap = serviceMap;
                break;
            case "CustomerSubCharges":
                classMap = subserviceMap;
                break;
            case "CustomerMiscCharges":
                classMap = miscchargeMap;
                break;
            default:
                AppUtils.extractJSONObject(jsonObject);
        }

        String key = classKeys.get(className);
        if (null != jsonObject.get(key)) {
//            System.out.println("key "+key+" object "+jsonObject);
            classMap.put((String) jsonObject.get(key), jsonObject);
        }

    }
}


