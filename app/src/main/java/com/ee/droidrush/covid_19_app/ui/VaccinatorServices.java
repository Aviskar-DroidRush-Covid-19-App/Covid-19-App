package com.ee.droidrush.covid_19_app.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ee.droidrush.covid_19_app.R;
import com.ee.droidrush.covid_19_app.VolleyFileDownloadRequest;
import com.ee.droidrush.covid_19_app.application.CowinDroidApplication;
import com.ee.droidrush.covid_19_app.application.LoginActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;


public class VaccinatorServices extends Fragment {




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vaccinator_services, container, false);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }




}