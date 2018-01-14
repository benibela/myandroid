package de.benibela.myandroid;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyAndroidApp extends Application  {
    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

       // NotificationService.startIfNecessary(this);
    }

    static MyAndroidApp instance;
    static Activity currentActivity;

}
