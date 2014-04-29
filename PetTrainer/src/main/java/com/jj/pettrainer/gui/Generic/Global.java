package com.jj.pettrainer.gui.Generic;

import android.content.SharedPreferences;

/**
 * Created by JCCP on 4/28/14.
 */
public class Global {

    public static String API_LOGIN_URL = "http://pettrainer.fau.jh.gg/api/login/";
    public static String API_GET_PETS_URL = "http://pettrainer.fau.jh.gg/api/pets/";
    public static String API_GET_PET_PROFILE = "http://pettrainer.fau.jh.gg/api/pets/";
    public static String API_GET_USER_PROFILE = "http://pettrainer.fau.jh.gg/api/users/";
    public static String API_REGISTER = "http://pettrainer.fau.jh.gg/api/users/";
    public static String API_ADD_PET = "http://pettrainer.fau.jh.gg/api/pets/";
    public static String API_GET_TASKS = "http://pettrainer.fau.jh.gg/api/pets/%s/tasks/";
    public static final String PREFS_NAME = "PetPrefsFile";
    public static SharedPreferences prefs;

    public static String getTaskUrl(int id) {
        return Global.API_GET_PETS_URL + id + "/tasks/";
    }



}
