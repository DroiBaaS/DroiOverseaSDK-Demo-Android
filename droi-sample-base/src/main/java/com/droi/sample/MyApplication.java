package com.droi.sample;

import android.app.Application;

import com.droi.mobileads.DroiSdk;


/**
 * Created by apple on 2016/11/9.
 */

public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        try {
            DroiSdk.initialize(this)
                    .setDebug(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
