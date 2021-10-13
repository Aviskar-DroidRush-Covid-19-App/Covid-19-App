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
import android.widget.TextView;
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


public class Vaccination_Services_Fragment extends Fragment {

    CowinDroidApplication cowinDroidApplication;
    RequestQueue requestQueue;
    TextView mob,ben_list;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        requestQueue= Volley.newRequestQueue(getContext());
        View root=inflater.inflate(R.layout.fragment_vaccination_services, container, false);
        mob=root.findViewById(R.id.mob);
        ben_list=root.findViewById(R.id.ben_list);
        cowinDroidApplication= (CowinDroidApplication) getActivity().getApplication();

        if(!cowinDroidApplication.isAlreadyLoggedIn())
        {
            requireLogin();
        }else
        {
            loadBenifishiaries(cowinDroidApplication.sessionStoagre.get("token"));
            mob.setText(mob.getText()+" "+cowinDroidApplication.sessionStoagre.get("mobile"));
        }

        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                Log.d("Login","Success");
                Toast.makeText(getContext(), "Logged in successfully", Toast.LENGTH_SHORT).show();
                loadBenifishiaries(cowinDroidApplication.sessionStoagre.get("token"));
                mob.setText(mob.getText()+" "+cowinDroidApplication.sessionStoagre.get("mobile"));

            }
        }
    }

    public void requireLogin()
    {
        Intent i = new Intent(getContext(), LoginActivity.class);
        i.putExtra("loginType","normalUser");
        startActivityForResult(i,100,null);
    }

    public void downloadCertificate(String token,String beniId)
    {   ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading beneficiaries...\nplease wait..");
        progressDialog.show();
        String downloadURL="https://cdn-api.co-vin.in/api/v2/registration/certificate/download?beneficiary_reference_id="+beniId;
        try {
            VolleyFileDownloadRequest fileDownloadRequest = new VolleyFileDownloadRequest(Request.Method.GET, downloadURL, new Response.Listener<byte[]>() {
                @Override
                public void onResponse(byte[] response) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
                    Log.d("Volley", String.valueOf(response));
                    try{
                        if(response!=null)
                        {
                            FileOutputStream outputStream;
                            String name = beniId+".pdf";
                            outputStream= new FileOutputStream(Environment.getExternalStorageDirectory()+"/"+name);
                            outputStream.write(response);
                            outputStream.close();
                            Toast.makeText(getContext(), "Downloaded....", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e)
                    {
                        Toast.makeText(getContext(), "Could not download file", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "error "+ error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
            }, null){

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String ,String> mp = new HashMap<>();
                    mp.put("Authorization","Bearer "+token);
                    //mp.put("accept","application/json");
                    Log.d("Volley",mp.toString());
                    return mp;
                }
            };
            requestQueue.add(fileDownloadRequest);
        }catch (Exception e)
        {
            e.printStackTrace();
            progressDialog.dismiss();
        }
    }

    public void loadBenifishiaries(String token)
    {                    //token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiIxNGMxNTVkOS1mZDU5LTQwN2ItOWIyMC05NWY1MDEzZjI3NmYiLCJ1c2VyX2lkIjoiMTRjMTU1ZDktZmQ1OS00MDdiLTliMjAtOTVmNTAxM2YyNzZmIiwidXNlcl90eXBlIjoiQkVORUZJQ0lBUlkiLCJtb2JpbGVfbnVtYmVyIjo5NjE2MjgwMDg4LCJiZW5lZmljaWFyeV9yZWZlcmVuY2VfaWQiOjUzNDE4NDY0Mjc4NjUwLCJzZWNyZXRfa2V5IjoiYjVjYWIxNjctNzk3Ny00ZGYxLTgwMjctYTYzYWExNDRmMDRlIiwic291cmNlIjoiY293aW4iLCJ1YSI6Ik1vemlsbGEvNS4wIChXaW5kb3dzIE5UIDEwLjA7IFdpbjY0OyB4NjQpIEFwcGxlV2ViS2l0LzUzNy4zNiAoS0hUTUwsIGxpa2UgR2Vja28pIENocm9tZS85My4wLjQ1NzcuODIgU2FmYXJpLzUzNy4zNiBFZGcvOTMuMC45NjEuNTIiLCJkYXRlX21vZGlmaWVkIjoiMjAyMS0xMC0xM1QwMTo0NDozOC4wNDhaIiwiaWF0IjoxNjM0MDg5NDc4LCJleHAiOjE2MzQwOTAzNzh9.Z2ldhYWUfrKGjml2CtntGjLPArvH8PDlrG9jHeEOshc";

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading beneficiaries...\nplease wait..");
        progressDialog.show();
        String linkForBenificiaries="https://cdn-api.co-vin.in/api/v2/appointment/beneficiaries";
        try{
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,linkForBenificiaries, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Volley",response.toString());
                    try{
                        JSONArray all = response.getJSONArray("beneficiaries");
                        for(int i=0;i<all.length();i++)
                        {
                            JSONObject ben = all.getJSONObject(i);
                            Log.d("Volley",ben.getString("name")+ben.getString("birth_year")+ben.getString("beneficiary_reference_id"));
                            ben_list.setText('\n'+ben.getString("name")+'\n'+ben.getString("birth_year")+ben.getString("beneficiary_reference_id"));
                            //downloadCertificate(token,ben.getString("beneficiary_reference_id"));
                        }

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }



                    progressDialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    if(error.networkResponse.statusCode==401)
                        requireLogin();
                    //Log.d(getHeaders().toString());
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String ,String> mp = new HashMap<>();
                    mp.put("Authorization","Bearer "+token);
                    mp.put("accept","application/json");
                    Log.d("Volley",mp.toString());
                    return mp;
                }

            };

            requestQueue.add(jsonObjectRequest);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}