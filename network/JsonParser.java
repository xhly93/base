/**
 * Copyright (C) 2013 Tendyron company.                                  *
 * All rights reserved.                           *
 *
 * @file JsonParser.java
 * @author chenlong (chenlong@tendyron.com)
 * <p>
 * 负责对通讯数据的解析类
 * @file JsonParser.java
 * @author chenlong (chenlong@tendyron.com)
 * <p>
 * 负责对通讯数据的解析类
 * @file JsonParser.java
 * @author chenlong (chenlong@tendyron.com)
 * <p>
 * 负责对通讯数据的解析类
 * @file JsonParser.java
 * @author chenlong (chenlong@tendyron.com)
 * <p>
 * 负责对通讯数据的解析类
 * @file JsonParser.java
 * @author chenlong (chenlong@tendyron.com)
 * <p>
 * 负责对通讯数据的解析类
 * @file JsonParser.java
 * @author chenlong (chenlong@tendyron.com)
 * <p>
 * 负责对通讯数据的解析类
 * @file JsonParser.java
 * @author chenlong (chenlong@tendyron.com)
 * <p>
 * 负责对通讯数据的解析类
 */
/**
 * @file JsonParser.java
 *
 * @author chenlong (chenlong@tendyron.com)
 *
 * 负责对通讯数据的解析类
 */

package com.ssj.user.Mode.network;

import android.util.Log;

import com.google.gson.Gson;
import com.ssj.user.Mode.Data.ServiceReturnLoginData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonParser {
    private static final String TAG = "JsonParser";
    private static Gson mGson = new Gson();

    /**
     * 转输入流为字符串
     *
     * @param input
     * @return时
     * @throws Exception
     */
    public static String formatStreamToString(InputStream input)
            throws Exception {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        String va = "";
        if (input != null) {
            try {
                br = new BufferedReader(new InputStreamReader(input, "utf-8"));
                while ((va = br.readLine()) != null) {
                    sb.append(va);
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new Exception(e.getMessage());
            }
        }
        Log.i(TAG, "From server  is:" + sb.toString());
        return sb.toString();
    }

    public static ServiceReturnLoginData getData(String str) {
        Log.i(TAG, "ServiceReturnLoginData:" + str);
        return mGson.fromJson(str, ServiceReturnLoginData.class);
    }

    /****
     * 对任意复杂的json格式数据的解析方法
     *
     * @param target
     * @return 一个HashMap/ArrayList 混合的复杂对象
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    public static Object parserRandomJsonFormat(String target)
            throws JSONException, UnsupportedEncodingException {
        HashMap<String, Object> map = null;
        ArrayList<Object> list = null;
        if (target != null && target.length() > 0) {
            target = target.trim();
            if ("[".equals(String.valueOf(target.charAt(0)))) {// jsonArray对象
                list = new ArrayList<Object>();
                JSONArray jArray = new JSONArray(target);
                parseStartJSONArrayFormat(jArray, list);
                return list;
            } else if ("{".equals(String.valueOf(target.charAt(0)))) {// jsonObject对象
                JSONObject object = new JSONObject(target);
                map = new HashMap<String, Object>();
                parseMutilJSONObjectFormat(object, map);
                return map;
            }
        }
        return null;
    }

    public static JSONArray parseJSONWithJSONObject(String jsonData) {
        try {
            //将json字符串jsonData装入JSON数组，即JSONArray
            JSONArray jsonArray = new JSONArray(jsonData);
            return jsonArray;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * 解析数组中包含数组、对象、基本数据的格式 如 [[]] 、[{}]、[1]
     *
     * @param jsonArray
     * @param list
     * @throws JSONException
     */
    public static void parseStartJSONArrayFormat(JSONArray jsonArray,
                                                 List<Object> list) throws JSONException {
        HashMap<String, Object> map = null;
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONArray array = jsonArray.getJSONArray(i);
                ArrayList<Object> sublist = new ArrayList<Object>();
                parseStartJSONArrayFormat(array, sublist);
                list.add(sublist);
            } catch (Exception e) {
                try {
                    map = new HashMap<String, Object>();
                    JSONObject object = jsonArray.getJSONObject(i);
                    parseMutilJSONObjectFormat(object, map);
                    list.add(map);
                } catch (Exception e1) {
                    list.add(jsonArray.get(i));
                }
            }
        }
    }

    /**
     * 解析json对象中包含的json对象 如{"name":{}}、{"name"：[]}、{"name":"1"}
     *
     * @param jsonObject
     * @param hashMap
     * @throws JSONException
     */
    public static void parseMutilJSONObjectFormat(JSONObject jsonObject,
                                                  HashMap<String, Object> hashMap) throws JSONException {
        JSONArray nameArray = jsonObject.names();
        for (int k = 0; k < nameArray.length(); k++) {
            String attrName = nameArray.getString(k);
            try {
                JSONArray objarray2 = jsonObject.getJSONArray(attrName);
                ArrayList<Object> sublist = new ArrayList<Object>();
                parseStartJSONArrayFormat(objarray2, sublist);
                hashMap.put(attrName, sublist);
            } catch (JSONException e) {
                try {
                    JSONObject objson2 = jsonObject.getJSONObject(attrName);
                    HashMap<String, Object> submap = new HashMap<String, Object>();
                    try {
                        parseMutilJSONObjectFormat(objson2, submap);
                    } catch (NullPointerException e1) {
                        e1.printStackTrace();
                    }
                    hashMap.put(attrName, submap);
                } catch (Exception e1) {
                    hashMap.put(attrName, jsonObject.get(attrName));
                }
            }
        }
    }

    /**
     * use to clear illegal char before send to server, by now, only " is
     * forbidden.
     *
     * @param mapDataIn: data that will be sent to server.
     */
    public static void cleanSendData(HashMap<String, String> mapDataIn) {
        if (null != mapDataIn && mapDataIn.size() > 0) {
            HashMap<String, String> tempDataMap = new HashMap<String, String>();
            for (String strKey : mapDataIn.keySet()) {
                String strValue = (String) mapDataIn.get(strKey);
                if (strValue.contains("\"")) {
                    // strValue = strValue.replaceAll("\"",
                    // "\\\\\"");
                    strValue = strValue.replace("\"", "\\\"");
                    tempDataMap.put(strKey, strValue);
                }
            }
            // update send data now.
            if (tempDataMap.size() > 0) {
                for (String strKey : tempDataMap.keySet()) {
                    mapDataIn.remove(strKey);
                    mapDataIn.put(strKey, tempDataMap.get(strKey));
                }
                tempDataMap.clear();
                tempDataMap = null;
            }
        }
    }
}
