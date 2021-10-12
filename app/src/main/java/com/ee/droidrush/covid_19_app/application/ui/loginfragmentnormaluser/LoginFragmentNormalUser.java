package com.ee.droidrush.covid_19_app.application.ui.loginfragmentnormaluser;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ee.droidrush.covid_19_app.R;


public class LoginFragmentNormalUser extends Fragment {

    private LoginFragmentNormalUserViewModel mViewModel;
    LinearLayout send,verification;
    public static LoginFragmentNormalUser newInstance() {
        return new LoginFragmentNormalUser();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.login_normal_user_fragment, container, false);
        send = root.findViewById(R.id.send);
        verification=root.findViewById(R.id.verify);
        Button sendOTP = root.findViewById(R.id.getOTP);
        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send.setVisibility(View.GONE);
                verification.setVisibility(View.VISIBLE);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LoginFragmentNormalUserViewModel.class);
        // TODO: Use the ViewModel
    }

}