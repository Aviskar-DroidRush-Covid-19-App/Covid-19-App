package com.ee.droidrush.covid_19_app.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ee.droidrush.covid_19_app.R;


public class Vaccinator_Services_Fragment extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vaccination_services, container, false);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}