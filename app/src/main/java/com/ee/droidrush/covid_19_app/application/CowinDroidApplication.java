package com.ee.droidrush.covid_19_app.application;

import android.app.Application;

import java.util.HashMap;

public class CowinDroidApplication extends Application {
    boolean isLoggedIn=false;
    //String mob;

    public boolean isAlreadyLoggedIn()
    {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public HashMap<String,String> sessionStoagre=new HashMap<>();


}
