package com.example.vidit.locateme;

import android.app.Application;
import android.content.Context;

/**
 * Created by vidit on 07-05-2016.
 */
public class MyApplication extends Application{
    private static MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

    }
    public static MyApplication getsInstance(){
        return sInstance;
    }
    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }
}
