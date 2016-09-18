package com.adolf.zhouzhuang.httpUtils;

/**
 * Created by adolf on 2016/9/13.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

public class GsonUtil {
    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    private GsonUtil() {
    }

    /**
     * 转成json
     *
     * @param object
     * @return
     */
    public static String oBjToJson(Object object) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }


    /**
     * json转bean
     * @param jsonObject
     * @param para
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T jsonToBean(JSONObject jsonObject ,String para, Class<T> cls) {
        T t = null;
        if (gson != null) {
            try {
                t = gson.fromJson(jsonObject.getString(para), cls);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return t;
    }

    public static String getExtendJson(JSONObject jsonObject ,String para){
        try {
            return jsonObject.getString(para);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 转成list
     * 泛型在编译期类型被擦除导致报错
     */
    public static <T> List<T> GsonToList(JSONObject jsonObject,String data ,Class<T> cls) {
        List<T> list = null;
        String extendJSon = getExtendJson(jsonObject,data);
        if (gson != null) {
            list = gson.fromJson(extendJSon, new TypeToken<List<T>>() {
            }.getType());
        }
        return list;
    }

    /**
     * 转成list
     * 解决泛型问题
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToList(JSONObject json,String data ,Class<T> cls) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(getExtendJson(json,data)).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }
        return list;
    }


    /**
     * 转成list中有map的
     *
     * @param gsonString
     * @return
     */
    public static <T> List<Map<String, T>> jsonToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());
        }
        return list;
    }

    /**
     * 转成map的
     *
     * @param gsonString
     * @return
     */
    public static <T> Map<String, T> jsonToMaps(String gsonString) {
        Map<String, T> map = null;
        if (gson != null) {
            map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }


}
