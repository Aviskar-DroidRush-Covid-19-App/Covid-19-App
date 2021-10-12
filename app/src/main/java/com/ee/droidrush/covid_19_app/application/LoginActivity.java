package com.ee.droidrush.covid_19_app.application;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ee.droidrush.covid_19_app.R;
import com.ee.droidrush.covid_19_app.application.ui.loginfragmentnormaluser.LoginFragmentNormalUser;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, LoginFragmentNormalUser.newInstance())
                    .commitNow();
        }
    }
}