package com.ee.droidrush.covid_19_app.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ee.droidrush.covid_19_app.HomeScreenActivity;
import com.ee.droidrush.covid_19_app.R;
import com.ee.droidrush.covid_19_app.data_container.Center;
import com.ee.droidrush.covid_19_app.data_container.CenterListAdpater;
import com.ee.droidrush.covid_19_app.data_container.Pair;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class DashboardFragment extends Fragment {
    com.google.android.material.button.MaterialButtonToggleGroup slotSearchModeChangeButton;
    LinearLayout mpin,mdistt;
    RequestQueue requestQueue;
    TextInputEditText pinCodeInput;
    AutoCompleteTextView stateList,disttList;
    Button search_slot_button;
    SimpleDateFormat sdf;
    String date;
    boolean inPinCodeSearchMode=false;
    RecyclerView slotSearchResultList;
    CenterListAdpater adpater;
    TextView no_center_available;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        requestQueue = Volley.newRequestQueue(getContext());
        View root= inflater.inflate(R.layout.fragment_dashboard, container, false);
        initilizeSlotSearchModule(root);
        Button book_your_slot = root.findViewById(R.id.book_your_slot);
        return root;
    }

    public void initilizeSlotSearchModule(View root)
    {
        slotSearchModeChangeButton = root.findViewById(R.id.toggle);
        mpin=root.findViewById(R.id.mpin);
        mdistt=root.findViewById(R.id.mdistt);
        stateList=root.findViewById(R.id.state);
        disttList=root.findViewById(R.id.distt);
        pinCodeInput=root.findViewById(R.id.pin_code_input);
        search_slot_button=root.findViewById(R.id.search_slot_button);
        slotSearchResultList=root.findViewById(R.id.slot_search_result_list);
        no_center_available=root.findViewById(R.id.no_centers_available);
        adpater = new CenterListAdpater();
        slotSearchResultList.setLayoutManager(new LinearLayoutManager(getContext()));
        slotSearchResultList.setAdapter(adpater);
        sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        sdf.setTimeZone(TimeZone.getDefault());
        date = sdf.format(new Date());
        slotSearchModeChangeButton.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if(isChecked)
                {
                    if(checkedId==R.id.distt_mode)
                    {
                        mdistt.setVisibility(View.VISIBLE);
                        loadStates();
                        mpin.setVisibility(View.GONE);
                        inPinCodeSearchMode=false;
                    }else {
                        mdistt.setVisibility(View.GONE);
                        mpin.setVisibility(View.VISIBLE);
                        inPinCodeSearchMode=true;
                    }
                }
            }
        });
        pinCodeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String s1 = s.toString();
                if(s1.matches("^[1-9][0-9]{2}[0-9]{3}$"))
                {
                    try{
                        int npin= Integer.parseInt(s1);
                    }catch (Exception e)
                    {
                        Toast.makeText(getContext(), "Please enter valid pin code", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        slotSearchModeChangeButton.check(R.id.distt_mode);
        search_slot_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSlots();
            }
        });
    }



    public void loadStates()
    {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://cdn-api.co-vin.in/api/v2/admin/location/states", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray mstateList = response.getJSONArray("states");
                    ArrayList<Pair> states = new ArrayList<>();
                    for (int i = 0; i < mstateList.length(); i++) {
                        JSONObject st = mstateList.getJSONObject(i);
                        Log.d("Volley","name: "+st.getString("state_name")+"state-id: "+st.getInt("state_id"));
                        states.add(new Pair(st.getString("state_name"), st.getInt("state_id")));
                    }
                    ArrayAdapter<Pair> adapter = new ArrayAdapter<>(getContext(),R.layout.item_drop,states);
                    stateList.setAdapter(adapter);
                    stateList.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }
                        @Override
                        public void afterTextChanged(Editable s) {
                            String st = s.toString();
                            if(st.length()>0)
                            {   int id = Integer.parseInt(st.substring(st.indexOf('(')+1,st.length()-1));
                                Toast.makeText(getContext(),"State choosen : "+st.substring(0,st.indexOf('(')), Toast.LENGTH_SHORT).show();
                                disttList.setText("");
                                loadDistricts(id);
                            }
                        }
                    });
                    progressDialog.dismiss();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley",error.toString());
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void loadDistricts(int stateId)
    {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        String link = "https://cdn-api.co-vin.in/api/v2/admin/location/districts/"+stateId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, link, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray mDisttList = response.getJSONArray("districts");
                    ArrayList<Pair> districts = new ArrayList<>();
                    for (int i = 0; i < mDisttList.length(); i++) {
                        JSONObject st = mDisttList.getJSONObject(i);
                        districts.add(new Pair(st.getString("district_name"),st.getInt("district_id")));
                    }
                    ArrayAdapter<Pair> adapter = new ArrayAdapter<>(getContext(),R.layout.item_drop,districts);
                    disttList.setAdapter(adapter);

                    progressDialog.dismiss();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley",error.toString());
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void loadSlots()
    {String link=null;
        if(inPinCodeSearchMode)
        {
            String s1 = pinCodeInput.getText().toString();
            if(s1.matches("^[1-9][0-9]{2}[0-9]{3}$"))
            {
                try{
                    int npin= Integer.parseInt(s1);
                    Toast.makeText(getContext(), "Finding by pin code", Toast.LENGTH_SHORT).show();
                    link = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode="+npin+"&date="+ date;

                }catch (Exception e)
                {
                    Toast.makeText(getContext(), "Please enter valid pin code", Toast.LENGTH_SHORT).show();
                    return;
                }
            }else {
                Toast.makeText(getContext(), "Please enter valid pin code", Toast.LENGTH_SHORT).show();
                return;
            }
        }else {
            String st = disttList.getText().toString();
            if(st.length()>0&&!st.contains("Select your"))
            {   int id = Integer.parseInt(st.substring(st.indexOf('(')+1,st.length()-1));
                link="https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id="+id+"&date="+ date;
                Toast.makeText(getContext(), "finding by districts", Toast.LENGTH_SHORT).show();
            }else
            {
                Toast.makeText(getContext(), "Select a district to find slot availability", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if(link==null)
            return;
        Log.d("Threader","final link"+link);

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, link, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray list = response.getJSONArray("centers");
                    ArrayList<Center> arrayList = new ArrayList<>();
                    adpater.setCenter(arrayList);
                    for(int i=0;i<list.length();i++)
                    {
                        String name = list.getJSONObject(i).getString("name");
                        Log.d("Threader","Results for center.."+name);
                        String state = list.getJSONObject(i).getString("state_name");
                        String district=list.getJSONObject(i).getString("district_name");
                        String address = list.getJSONObject(i).getString("address");
                        String fee_type= list.getJSONObject(i).getString("fee_type");
                        JSONArray sessions = list.getJSONObject(i).getJSONArray("sessions");
                        for(int j=0;j<sessions.length();j++) {
                            int total_avalable = sessions.getJSONObject(j).getInt("available_capacity");
                            String min_age_limit = sessions.getJSONObject(j).getString("min_age_limit");
                            int dose1 = sessions.getJSONObject(j).getInt("available_capacity_dose1");
                            int dose2 = sessions.getJSONObject(j).getInt("available_capacity_dose2");

                            Log.d("Threader", "total: " + total_avalable + " min_age_limit: " + min_age_limit + " dose1 " + dose1 + " dose2 " + dose2);
                            arrayList.add(new Center(name,address+'\n'+district+'\n'+state,name,fee_type,min_age_limit,dose1,dose2));
                        }
                    }
                    if(arrayList.size()>0)
                    {
                        adpater.setCenter(arrayList);
                        no_center_available.setVisibility(View.GONE);
                        slotSearchResultList.setVisibility(View.VISIBLE);
                    }else {
                        no_center_available.setVisibility(View.VISIBLE);
                        slotSearchResultList.setVisibility(View.GONE);
                    }
                    progressDialog.dismiss();
                }catch (Exception e)
                {
                    e.printStackTrace();
                    Log.d("Threader","error"+e.toString());
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley",error.toString());
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);

    }


}