package com.ee.droidrush.covid_19_app.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class Resources_Fragment extends Fragment {




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(com.ee.droidrush.covid_19_app.R.layout.fragment_resources, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}