package com.puc.util;

/**
 * Created by user on 4/5/2017.
 */


public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationLifecycleHandler handler = new ApplicationLifecycleHandler();
        registerActivityLifecycleCallbacks(handler);
        registerComponentCallbacks(handler);

    }

}