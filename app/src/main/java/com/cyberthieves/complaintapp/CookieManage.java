package com.cyberthieves.complaintapp;


import android.app.Application;

import java.net.CookieHandler;
import java.net.CookieManager;
// code for managing cookies

public class CookieManage extends Application {
    CookieManager cookiemanage;

    public void onCreate() {
        cookiemanage = new CookieManager();
        CookieHandler.setDefault(cookiemanage);
        super.onCreate();
    }
}