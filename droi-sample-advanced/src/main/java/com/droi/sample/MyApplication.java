package com.droi.sample;

import android.app.Application;

import com.droi.mobileads.DroiSdk;

/**
 * Created by zhouzhongbo on 2016/11/29.
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
