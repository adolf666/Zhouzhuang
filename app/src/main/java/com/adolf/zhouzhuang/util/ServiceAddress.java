package com.adolf.zhouzhuang.util;

/**
 * Created by adolf on 2016/8/28.
 */
public class ServiceAddress {
    public static String head = "http://139.196.217.52:8080/zhouzhuang/api/";

    public static String REGISTER = head + "register";
    public static String LOGIN = head + "login";
    public static String BANNER = head + "getBannerImages";
    public static String ALL_SPOTS = head + "getAllSpots";
    public static String NEWS_EXHIBITION_TEMPORARY = head +"getList";
    public static String COLLECTION = head + "collect";
    public static String COLLECTION_LIST = head + "getCollectList";
    public static String COLLECTION_CANCEL = head + "cancelCollect";
    public static String UPDGRAD_USER_INFO= head + "updgradeUserInfo";
    public static String UPDATE_PASSWORD = head + "updatePassword";
    public static String GET_STRATEGY = head + "getScenictravelList";
    public static String GET_APP_VERSION = head +"getAppVersion";
}
