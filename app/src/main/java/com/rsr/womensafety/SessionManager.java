package com.rsr.womensafety;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class SessionManager {

    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_LOGIN = "no";

    public static final String KEY_FIRSTNAME = "firstname";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_AGE = "age";
    public static final String KEY_MOBILE = "mobile";
    public static final String KEY_ADDRESS = "address";


    public SessionManager(Context _context){

        context = _context;
        userSession = _context.getSharedPreferences("userLoginSession", Context.MODE_PRIVATE);
        editor = userSession.edit();

    }

    public void createLoginSession(String fname, String lname, String age, String mobile, String address){

        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_FIRSTNAME, fname);
        editor.putString(KEY_LASTNAME, lname);
        editor.putString(KEY_AGE, age);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_ADDRESS, address);

        editor.commit();

    }

    public HashMap<String, String> getUserData(){

        HashMap<String, String> userData = new HashMap<String, String>();

        userData.put(KEY_FIRSTNAME, userSession.getString(KEY_FIRSTNAME, null));
        userData.put(KEY_LASTNAME, userSession.getString(KEY_LASTNAME, null));
        userData.put(KEY_AGE, userSession.getString(KEY_AGE, null));
        userData.put(KEY_MOBILE, userSession.getString(KEY_MOBILE, null));
        userData.put(KEY_ADDRESS, userSession.getString(KEY_ADDRESS, null));

        return userData;
    }

    public boolean checkLogin(){

        if(userSession.getBoolean(IS_LOGIN, true)){
            return true;
        }
        else {
            return false;
        }

    }

    public void logout(){
        editor.putBoolean(IS_LOGIN, false);
        editor.clear();
        editor.commit();
    }

    public void saveArrayList(ArrayList<String> list, String key){
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public ArrayList<String> getArrayList(String key){
        Gson gson = new Gson();
        String json = userSession.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

}
