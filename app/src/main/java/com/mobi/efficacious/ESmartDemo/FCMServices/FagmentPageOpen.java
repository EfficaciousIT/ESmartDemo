package com.mobi.efficacious.ESmartDemo.FCMServices;
import android.app.Application;

/**
 * Author: Kartik Sharma
 * Created on: 10/16/2016 , 9:35 PM
 * Project: FirebaseChat
 */

public class FagmentPageOpen extends Application {
    private static boolean sIsChatActivityOpen = false;

    public static boolean isChatActivityOpen() {
        return sIsChatActivityOpen;
    }

    public static void setChatActivityOpen(boolean isChatActivityOpen) {
        FagmentPageOpen.sIsChatActivityOpen = isChatActivityOpen;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
