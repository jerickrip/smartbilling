package com.ni2.maskatel.smartbilling;

import com.ni2.maskatel.smartbilling.utils.APIUtils;
import com.ni2.maskatel.smartbilling.utils.AppUtils;
import com.ni2.maskatel.smartbilling.utils.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

public class Main {
    private static String baseURL;
    private static Properties properties;

    public static void main(String[] args) {
        String token;
        Date start = new Date();

        // Misc
        JSONArray datas;
        JSONObject data;
        String objectClass;
        String key;

        // Load parameters and try to login.
        loadProperties();
        token = AppUtils.login(properties);

        // If we were able to login, let's extract data.
        if (null != token) {
            try {
                APIUtils.getAllObjectsDetailsForClass("Customer", baseURL, token);
                APIUtils.getAllObjectsDetailsForClass("CustomerLocations", baseURL, token);
//                APIUtils.getAllObjectsDetailsForClass("CustomerAddress", baseURL, token);
                APIUtils.getAllObjectsDetailsForClass("CustomerProducts", baseURL, token);
                APIUtils.getAllObjectsDetailsForClass("CustomerSubCharges", baseURL, token);
//                APIUtils.getAllObjectsDetailsForClass("CustomerMiscCharges", baseURL, token);
                APIUtils.getAllObjectsDetailsForClass("CustomerSubscriptions", baseURL, token);
//                APIUtils.getAllObjectsDetailsForClass("CustomerContacts", baseURL, token);

                FileUtils.setDataMaps(APIUtils.getMaps());
                FileUtils.writeFile("files/Customers", "Customer", Class.forName("com.ni2.maskatel.smartbilling.utils.FileUtils"));
                FileUtils.writeFile("files/CustomerLocations", "CustomerLocations", Class.forName("com.ni2.maskatel.smartbilling.utils.FileUtils"));
//                FileUtils.writeFile("files/CustomerAddress", "CustomerAddress", Class.forName("com.ni2.maskatel.smartbilling.utils.FileUtils"));
                FileUtils.writeFile("files/CustomerSubscriptions", "CustomerSubscriptions", Class.forName("com.ni2.maskatel.smartbilling.utils.FileUtils"));
                FileUtils.writeFile("files/CustomerProducts", "CustomerProducts", Class.forName("com.ni2.maskatel.smartbilling.utils.FileUtils"));
                FileUtils.writeFile("files/CustomerSubCharges", "CustomerSubCharges", Class.forName("com.ni2.maskatel.smartbilling.utils.FileUtils"));
                FileUtils.writeFile("files/CustomerMiscCharges", "CustomerMiscCharges", Class.forName("com.ni2.maskatel.smartbilling.utils.FileUtils"));
//                FileUtils.writeFile("files/CustomerContacts", "CustomerContacts", Class.forName("com.ni2.maskatel.smartbilling.utils.FileUtils"));
//            APIUtils.writePackageFile();
            } catch (Exception e) {
                System.out.println("void main 01: Uncaught exception.");
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        AppUtils.printTimeDiff(start);
    }

    private static void loadProperties() {
        properties = new Properties();
        InputStream input = null;
        try {
            // read configuration file
            input = new FileInputStream("config.properties");
            properties.load(input);
            baseURL = properties.getProperty("baseURL");
            System.out.println("===================");
            System.out.println("Using the following parameters:");
            for (Map.Entry e : properties.entrySet())
                System.out.println(e.getKey() + ": " + e.getValue());
            System.out.println("===================");
        } catch (IOException ex) {
            System.out.println("No configuration file present.");
            System.out.println("Place 'config.properties' in the same directory as JAR file.");
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println("No configuration file present.");
                    System.out.println("Place 'config.properties' in the same directory as JAR file.");
                    e.printStackTrace();
                }
            }
        }
    }
}