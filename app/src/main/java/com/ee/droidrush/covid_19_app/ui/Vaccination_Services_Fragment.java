package com.ee.droidrush.covid_19_app.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.ee.droidrush.covid_19_app.data_container.Pair;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Vaccination_Services_Fragment extends Fragment {

    CowinDroidApplication cowinDroidApplication;
    RequestQueue requestQueue;
    LinearLayout conainer_benificiary;
    Button add_new_benificiary;

    //TextView mob,ben_list;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        requestQueue= Volley.newRequestQueue(getContext());
        View root=inflater.inflate(R.layout.fragment_vaccination_services, container, false);

        cowinDroidApplication= (CowinDroidApplication) getActivity().getApplication();

        if(!cowinDroidApplication.isAlreadyLoggedIn())
        {
            requireLogin();
        }else
        {
            loadBenifishiaries(cowinDroidApplication.sessionStoagre.get("token"));

        }
        conainer_benificiary=root.findViewById(R.id.container_ben);
        add_new_benificiary = root.findViewById(R.id.add_member);
        add_new_benificiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_new_ben_reg_form();
            }
        });
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
               // mob.setText(mob.getText()+" "+cowinDroidApplication.sessionStoagre.get("mobile"));

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
            VolleyFileDownloadRequest fileDownloadRequest = new VolleyFileDownloadRequest(Request.Method.GET, downloadURL, response -> {
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
            }, error -> {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "error "+ error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }, null){

                @Override
                public Map<String, String> getHeaders() {
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
    {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading beneficiaries...\nplease wait..");
        progressDialog.show();
        String linkForBenificiaries="https://cdn-api.co-vin.in/api/v2/appointment/beneficiaries";
        try{
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,linkForBenificiaries, null, response -> {
                Log.d("Volley",response.toString());
                try{
                    JSONArray all = response.getJSONArray("beneficiaries");
                    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    conainer_benificiary.removeAllViews();
                    for(int i=0;i<all.length();i++)
                    {
                        View v = inflater.inflate(R.layout.item_layout_beneficiary,conainer_benificiary,true);
                        JSONObject ben = all.getJSONObject(i);
                        ((TextView)v.findViewById(R.id.name_ben)).setText(ben.getString("name"));
                        ((TextView)v.findViewById(R.id.year_of_birth)).setText(String.format("Year of Birth : %s", ben.getString("birth_year")));
                        ((TextView)v.findViewById(R.id.photo_it_type)).setText(String.format("Photo ID: %s", ben.getString("photo_id_type")));
                        ((TextView)v.findViewById(R.id.photo_id_number)).setText(String.format("ID number: %s", ben.getString("photo_id_number")));
                        ((TextView)v.findViewById(R.id.ref_id)).setText(String.format("REF ID: %s", ben.getString("beneficiary_reference_id")));
                        ((TextView)v.findViewById(R.id.sec_code)).setText(String.format("Secret Code: %s", ben.getString("beneficiary_reference_id").substring(ben.getString("beneficiary_reference_id").length()-4)));
                        String mob = ben.getString("mobile_number");
                        String vaccination_status=ben.getString("vaccination_status");
                        String dose1_date = ben.getString("dose1_date");
                        String dose2_date = ben.getString("dose2_date");
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }, error -> {
                error.printStackTrace();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                if(error.networkResponse.statusCode==401)
                    requireLogin();
                //Log.d(getHeaders().toString());
            }){
                @Override
                public Map<String, String> getHeaders() {
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
            Toast.makeText(getContext(), "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();
        }
    }





    public void addBeneficiary(JSONObject jsonObject,String token)
    {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Registering...\nplease wait..");
        String url = "https://cdn-api.co-vin.in/api/v2/registration/beneficiary/new";
        progressDialog.show();
        try{
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(error.networkResponse.statusCode==401)
                        requireLogin();
                    if(error.networkResponse.statusCode==400)
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String ,String> mp = new HashMap<>();
                    mp.put("Authorization","Bearer "+token);
                    mp.put("accept","application/json");
                    //Log.d("Volley",mp.toString());
                    return mp;
                }
            };
        }catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getContext(), "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();
        }
    }


    public void show_new_ben_reg_form()
    {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.layout_new_benificiary_add);
        Button reg = bottomSheetDialog.findViewById(R.id.btn_register);
        AutoCompleteTextView photo_id_types = bottomSheetDialog.findViewById(R.id.photo_id_type_input);
        TextInputEditText year_of_birth_Input,id_number_Input,nameInput;
        id_number_Input=bottomSheetDialog.findViewById(R.id.photo_id_num_input);
        year_of_birth_Input=bottomSheetDialog.findViewById(R.id.input_year_of_birth);
        RadioGroup genderInput = bottomSheetDialog.findViewById(R.id.input_gender);
        nameInput=bottomSheetDialog.findViewById(R.id.name_input);
        ArrayList<Pair> docs = new ArrayList<>();
        docs.add(new Pair("Aadhar Card",1));
        docs.add(new Pair("Driving License",2));
        docs.add(new Pair("Pan Card",6));
        docs.add(new Pair("Passport",8));
        docs.add(new Pair("Ration Card With Photo",16));
        docs.add(new Pair("Voter ID",12));
        docs.add(new Pair("Pension Passbook",9));
        ArrayAdapter<Pair> adapter = new ArrayAdapter<>(getContext(),R.layout.item_drop,docs);
        photo_id_types.setAdapter(adapter);
        bottomSheetDialog.show();

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Registering...\nplease wait..");
                String url = "https://cdn-api.co-vin.in/api/v2/registration/beneficiary/new";
                progressDialog.show();

                //check if form is filled correctly
                String year_of_birth = year_of_birth_Input.getText().toString();
                String name = nameInput.getText().toString();
                String id_number = id_number_Input.getText().toString();
                int gender = 1;
                if(genderInput.getCheckedRadioButtonId()==R.id.female)
                    gender=2;
                else if(genderInput.getCheckedRadioButtonId()==R.id.others)
                    gender=3;
                int idType= -1;
                String idTypeS = photo_id_types.getText().toString();
                Log.d("Volley",idTypeS);

                if(idTypeS.length()>0&&!idTypeS.contains("Photo ID"))
                {
                    idType= Integer.parseInt(idTypeS.substring(idTypeS.indexOf('(')+1,idTypeS.indexOf(')')));
                    Log.d("Volley",idTypeS.substring(idTypeS.indexOf('(')+1, idTypeS.indexOf(')')));
                }

                if(id_number.length()==0||idType==-1||year_of_birth.length()==0||name.length()==0)
                {
                    Toast.makeText(getContext(), "Entered data is not valid", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }
                try{
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("gender_id",gender);
                    jsonObject.put("photo_id_number",id_number);
                    jsonObject.put("birth_year",year_of_birth);
                    jsonObject.put("consent_version","V1");
                    jsonObject.put("name",name);
                    jsonObject.put("photo_id_type",idType);
                    Log.d("Volley",jsonObject.toString());
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, response -> {
                        bottomSheetDialog.dismiss();
                        Toast.makeText(getContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                        loadBenifishiaries(cowinDroidApplication.sessionStoagre.get("token"));
                        progressDialog.dismiss();
                    }, error -> {
                        if(error.networkResponse!=null)
                        {
                            if(error.networkResponse.statusCode==401)
                                requireLogin();
                            if(error.networkResponse.statusCode==400)
                                Toast.makeText(getContext(), "Entered data is not valid/Benificiary is already registered", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }){
                        @Override
                        public Map<String, String> getHeaders() {
                            Map<String ,String> mp = new HashMap<>();
                            mp.put("Authorization","Bearer "+cowinDroidApplication.sessionStoagre.get("token"));
                            mp.put("accept","application/json");
                            return mp;
                        }
                    };

                    requestQueue.add(jsonObjectRequest);
                }catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }



}