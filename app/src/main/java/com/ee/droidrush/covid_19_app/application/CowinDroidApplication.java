package com.ee.droidrush.covid_19_app.application;

import android.app.Application;

public class CowinDroidApplication extends Application {
    boolean isLoggedIn=false;

    public boolean isAlreadyLoggedIn()
    {
        return isLoggedIn;
    }

}
