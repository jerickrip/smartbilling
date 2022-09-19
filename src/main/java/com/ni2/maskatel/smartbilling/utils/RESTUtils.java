package com.ni2.maskatel.smartbilling.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.*;
import java.util.*;

// Sample code from https://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/
// http://stackoverflow.com/questions/4205980/java-sending-http-parameters-via-post-method-easily
public class RESTUtils {

    public static JSONObject httpGET(String url, String token, Map<String, String> params) throws Exception {
        String request_params = "";
        for(String e: params.keySet()){
            if (0 != request_params.length())
                request_params += "&";
            request_params += e + "=" + params.get(e);
        }
        if(0 != request_params.length())
            url += "?" + request_params;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // Configure HttpRequest
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Basic " + token + ":");
        //con.setUseCaches(false); Might do the same as bellow
        con.setRequestProperty("cache-control", "no-cache");

        return getHttpResponse(con);
    }

    public static JSONObject httpPOST(String url, Map<String, String> params) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Configure parameters
        String request_params = "";
        for(String e: params.keySet()){
            if (0 != request_params.length())
                request_params += "&";
            request_params += e + "=" + params.get(e);
        }

        // Configure HttpRequest
        con.setRequestMethod("POST");
        con.setRequestProperty("cache-control", "no-cache");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setRequestProperty("Content-Length", String.valueOf(request_params.getBytes("UTF-8").length));

        // Send parameters
        con.setDoOutput(true);
        con.getOutputStream().write(request_params.getBytes("UTF-8"));

        return getHttpResponse(con);
    }

    private static JSONObject getHttpResponse(HttpURLConnection con) throws Exception {
        // Get Response and parse
        if(200 != con.getResponseCode()) {
            System.out.println("Got error for URL: " + con.getURL());
            System.out.println("Response code: " + con.getResponseCode());
            return null;
        }

        // Read response from request
        Reader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (int c; (c = in.read()) >= 0;)
            sb.append((char)c);

        // Close connection
        con.disconnect();
        // Parse and return response
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(sb.toString());
    }
}
