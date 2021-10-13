package com.ee.droidrush.covid_19_app.application.ui.loginfragmentnormaluser;

import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;


public class LoginFragmentNormalUser extends Fragment {

    String txnID;
    LinearLayout send,verification;
    RequestQueue requestQueue;
    public static LoginFragmentNormalUser newInstance() {
        return new LoginFragmentNormalUser();
    }
    EditText mobile,otp;
    CowinDroidApplication application;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.login_normal_user_fragment, container, false);
        application= (CowinDroidApplication) getActivity().getApplication();
        requestQueue= Volley.newRequestQueue(getContext());
        send = root.findViewById(R.id.send);
        verification=root.findViewById(R.id.verify);
        Button sendOTP = root.findViewById(R.id.getOTP);
        mobile = root.findViewById(R.id.mobile);
        mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()==10)
                    sendOTP.setEnabled(true);
                else sendOTP.setEnabled(false);
            }
        });
        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send.setVisibility(View.GONE);
                //verification.setVisibility(View.VISIBLE);
                sendOTP(mobile.getText().toString());
            }
        });


        Button verifyOTPButton = root.findViewById(R.id.verifyotp);

        otp=root.findViewById(R.id.otp_enter);
        otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()==6)
                    verifyOTPButton.setEnabled(true);
                else verifyOTPButton.setEnabled(false);
            }
        });

        verifyOTPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOTP(otp.getText().toString());
            }
        });


       // loadBenifishiaries("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiIxNGMxNTVkOS1mZDU5LTQwN2ItOWIyMC05NWY1MDEzZjI3NmYiLCJ1c2VyX2lkIjoiMTRjMTU1ZDktZmQ1OS00MDdiLTliMjAtOTVmNTAxM2YyNzZmIiwidXNlcl90eXBlIjoiQkVORUZJQ0lBUlkiLCJtb2JpbGVfbnVtYmVyIjo5NjE2MjgwMDg4LCJiZW5lZmljaWFyeV9yZWZlcmVuY2VfaWQiOjUzNDE4NDY0Mjc4NjUwLCJzZWNyZXRfa2V5IjoiYjVjYWIxNjctNzk3Ny00ZGYxLTgwMjctYTYzYWExNDRmMDRlIiwic291cmNlIjoiY293aW4iLCJ1YSI6Ik1vemlsbGEvNS4wIChXaW5kb3dzIE5UIDEwLjA7IFdpbjY0OyB4NjQpIEFwcGxlV2ViS2l0LzUzNy4zNiAoS0hUTUwsIGxpa2UgR2Vja28pIENocm9tZS85My4wLjQ1NzcuODIgU2FmYXJpLzUzNy4zNiBFZGcvOTMuMC45NjEuNTIiLCJkYXRlX21vZGlmaWVkIjoiMjAyMS0xMC0xM1QwMjoyNzo1NC4wNTNaIiwiaWF0IjoxNjM0MDkyMDc0LCJleHAiOjE2MzQwOTI5NzR9.1gZtohzQkLgb2J-wOPeyilx5XnBYyaACz_j-tuApPUA");
        return root;
    }




    public void sendOTP(String mobile)
    {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sending OTP...\nplease wait..");
        progressDialog.show();
        String sendotpurl="https://cdn-api.co-vin.in/api/v2/auth/generateMobileOTP";
        try {
        JSONObject mobileObject = new JSONObject();

        mobileObject.put("secret","U2FsdGVkX1+5EC4/EhRINSuzA4P8EDreqz6MuhTRBDlvrUtQUY/68g0Zdo2CSE5cAH8U5kTudB/wjAcavDponA==");
        mobileObject.put("mobile", mobile);
        Log.d("Volley",mobileObject.toString());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, sendotpurl, mobileObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Volley",response.toString());
                    try {
                        Log.d("Volley",response.getString("txnId"));
                        txnID=response.getString("txnId");
                        send.setVisibility(View.GONE);
                        verification.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });

            requestQueue.add(jsonObjectRequest);
        }
        catch (Exception e)
        {
        e.printStackTrace();
        progressDialog.dismiss();
        }


    }


    public void verifyOTP(String otp)
    {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Verifying OTP...\nplease wait..");
        progressDialog.show();
        String verifyotpurl="https://cdn-api.co-vin.in/api/v2/auth/validateMobileOtp";

        try {
            JSONObject OTPObject = new JSONObject();
            OTPObject.put("otp",getEncryptedOTPstr(otp));
            OTPObject.put("txnId",txnID);

            Log.d("Volley",OTPObject.toString());

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, verifyotpurl, OTPObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        String token = response.getString("token");
                        Log.d("Volley","token "+token);
                        application.sessionStoagre.put("token",token);
                        application.sessionStoagre.put("mobile",mobile.getText().toString());
                        application.setLoggedIn(true);
                        Toast.makeText(getContext(), "Logged in successfully", Toast.LENGTH_SHORT).show();
                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();
                        //loadBenifishiaries(token);
                        //loadBenifishiaries();
                        //loadBenifishiaries("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiIxNGMxNTVkOS1mZDU5LTQwN2ItOWIyMC05NWY1MDEzZjI3NmYiLCJ1c2VyX2lkIjoiMTRjMTU1ZDktZmQ1OS00MDdiLTliMjAtOTVmNTAxM2YyNzZmIiwidXNlcl90eXBlIjoiQkVORUZJQ0lBUlkiLCJtb2JpbGVfbnVtYmVyIjo5NjE2MjgwMDg4LCJiZW5lZmljaWFyeV9yZWZlcmVuY2VfaWQiOjUzNDE4NDY0Mjc4NjUwLCJzZWNyZXRfa2V5IjoiYjVjYWIxNjctNzk3Ny00ZGYxLTgwMjctYTYzYWExNDRmMDRlIiwic291cmNlIjoiY293aW4iLCJ1YSI6Ik1vemlsbGEvNS4wIChXaW5kb3dzIE5UIDEwLjA7IFdpbjY0OyB4NjQpIEFwcGxlV2ViS2l0LzUzNy4zNiAoS0hUTUwsIGxpa2UgR2Vja28pIENocm9tZS85My4wLjQ1NzcuODIgU2FmYXJpLzUzNy4zNiBFZGcvOTMuMC45NjEuNTIiLCJkYXRlX21vZGlmaWVkIjoiMjAyMS0xMC0xM1QwMTo0NDozOC4wNDhaIiwiaWF0IjoxNjM0MDg5NDc4LCJleHAiOjE2MzQwOTAzNzh9.Z2ldhYWUfrKGjml2CtntGjLPArvH8PDlrG9jHeEOshc");
                    }catch (Exception e)
                    {
                        Log.d("Volley",response.toString());
                    }
                    progressDialog.dismiss();
                }
            }, error -> {
                error.printStackTrace();
                String mes = null;
                switch (error.networkResponse.statusCode)
                {
                    case 400:
                        mes="Invalid OTP";break;
                    case 401:
                        mes="Unothorised access";break;
                    case 500:
                        mes="Internal server error";

                }
                Toast.makeText(getContext(), mes, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            });

            requestQueue.add(jsonObjectRequest);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            progressDialog.dismiss();
        }

    }


    public static String getEncryptedOTPstr(String o)
    {
        try{
            MessageDigest md  = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(o.getBytes("utf-8"));
            StringBuilder sb = new StringBuilder();
            for(int i=0;i<hash.length;i++)
            {
                String h = Integer.toHexString(0xff&hash[i]);
                if(h.length()==1)
                    sb.append('0');
                sb.append(h);
            }
            return sb.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }






    //eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiIxNGMxNTVkOS1mZDU5LTQwN2ItOWIyMC05NWY1MDEzZjI3NmYiLCJ1c2VyX2lkIjoiMTRjMTU1ZDktZmQ1OS00MDdiLTliMjAtOTVmNTAxM2YyNzZmIiwidXNlcl90eXBlIjoiQkVORUZJQ0lBUlkiLCJtb2JpbGVfbnVtYmVyIjo5NjE2MjgwMDg4LCJiZW5lZmljaWFyeV9yZWZlcmVuY2VfaWQiOjUzNDE4NDY0Mjc4NjUwLCJzZWNyZXRfa2V5IjoiYjVjYWIxNjctNzk3Ny00ZGYxLTgwMjctYTYzYWExNDRmMDRlIiwic291cmNlIjoiY293aW4iLCJ1YSI6Ik1vemlsbGEvNS4wIChXaW5kb3dzIE5UIDEwLjA7IFdpbjY0OyB4NjQpIEFwcGxlV2ViS2l0LzUzNy4zNiAoS0hUTUwsIGxpa2UgR2Vja28pIENocm9tZS85My4wLjQ1NzcuODIgU2FmYXJpLzUzNy4zNiBFZGcvOTMuMC45NjEuNTIiLCJkYXRlX21vZGlmaWVkIjoiMjAyMS0xMC0xM1QwMTo0NDozOC4wNDhaIiwiaWF0IjoxNjM0MDg5NDc4LCJleHAiOjE2MzQwOTAzNzh9.Z2ldhYWUfrKGjml2CtntGjLPArvH8PDlrG9jHeEOshc
    //eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiIxNGMxNTVkOS1mZDU5LTQwN2ItOWIyMC05NWY1MDEzZjI3NmYiLCJ1c2VyX2lkIjoiMTRjMTU1ZDktZmQ1OS00MDdiLTliMjAtOTVmNTAxM2YyNzZmIiwidXNlcl90eXBlIjoiQkVORUZJQ0lBUlkiLCJtb2JpbGVfbnVtYmVyIjo5NjE2MjgwMDg4LCJiZW5lZmljaWFyeV9yZWZlcmVuY2VfaWQiOjUzNDE4NDY0Mjc4NjUwLCJzZWNyZXRfa2V5IjoiYjVjYWIxNjctNzk3Ny00ZGYxLTgwMjctYTYzYWExNDRmMDRlIiwic291cmNlIjoiY293aW4iLCJ1YSI6Ik1vemlsbGEvNS4wIChXaW5kb3dzIE5UIDEwLjA7IFdpbjY0OyB4NjQpIEFwcGxlV2ViS2l0LzUzNy4zNiAoS0hUTUwsIGxpa2UgR2Vja28pIENocm9tZS85My4wLjQ1NzcuODIgU2FmYXJpLzUzNy4zNiBFZGcvOTMuMC45NjEuNTIiLCJkYXRlX21vZGlmaWVkIjoiMjAyMS0xMC0xM1QwMTo0NDozOC4wNDhaIiwiaWF0IjoxNjM0MDg5NDc4LCJleHAiOjE2MzQwOTAzNzh9.Z2ldhYWUfrKGjml2CtntGjLPArvH8PDlrG9jHeEOshc

}