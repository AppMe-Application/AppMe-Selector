package com.appme.story;

import android.app.Application;
import android.content.Context;

import com.appme.story.engine.app.analytics.CrashHandler;

public class AppController extends Application {

    private static AppController isInstance;
    private static Context mContext;
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        isInstance = this;
        mContext = this;
        /*if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);*/
        CrashHandler.init(this);
    }
    
    public static synchronized AppController getInstance() {
        return isInstance;
    }

    public static Context getContext() {
        return mContext;
    }
}

