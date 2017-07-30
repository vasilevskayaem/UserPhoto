package com.photo.user.userphoto.settings;
import android.content.SharedPreferences;
import android.graphics.Color;

/**
 * Created by Kate on 7/29/17.
 */

public class Settings {

    public static String SHARED_PREFERENCE_COLOR= "image_color";
    public static String SHARED_PREFERENCE_LOADING_IN_PROCESS= "loading";
    public static String SHARED_PREFERENCE_NEED_UPDATE= "need_update";
    public static String SHARED_IMAGE_URL= "image_url";
    public static final String PREFS_NAME = "Settings";

    public static Integer getColor(SharedPreferences prefs){
        String color =prefs.getString(SHARED_PREFERENCE_COLOR,null);
        if(color!=null){
            return Color.parseColor(color);
        }
        else{
            return null;
        }
    }

    public static void setColor(SharedPreferences prefs, int color){
        SharedPreferences.Editor ed = prefs.edit();
        ed.putString(SHARED_PREFERENCE_COLOR, "#"+ Integer.toHexString(color));
        ed.apply();
    }

    public static void clearColor(SharedPreferences prefs){
        SharedPreferences.Editor ed = prefs.edit();
        ed.remove(SHARED_PREFERENCE_COLOR);
        ed.apply();
    }

    public static boolean isLoadingInProcess(SharedPreferences prefs){
        boolean isLoading =prefs.getBoolean(SHARED_PREFERENCE_LOADING_IN_PROCESS,false);
        return isLoading;
    }

    public static void setLoadingInProcess(SharedPreferences prefs, boolean isLoadingInProcess){
        SharedPreferences.Editor ed = prefs.edit();
        ed.putBoolean(SHARED_PREFERENCE_LOADING_IN_PROCESS, isLoadingInProcess);
        ed.apply();
    }

    public static boolean isNeedUpdate(SharedPreferences prefs){
        boolean needUpdate =prefs.getBoolean(SHARED_PREFERENCE_NEED_UPDATE,false);
        return needUpdate;
    }

    public static void setNeedUpdate(SharedPreferences prefs, boolean needUpdate){
        SharedPreferences.Editor ed = prefs.edit();
        ed.putBoolean(SHARED_PREFERENCE_NEED_UPDATE, needUpdate);
        ed.apply();
    }

    public static String getImageUrl(SharedPreferences prefs){
        String imageUrl = prefs.getString(SHARED_IMAGE_URL,null);
        return imageUrl;
    }

    public static void setImageUrl(SharedPreferences prefs, String imageUrl){
        SharedPreferences.Editor ed = prefs.edit();
        ed.putString(SHARED_IMAGE_URL, imageUrl);
        ed.apply();
    }
}
