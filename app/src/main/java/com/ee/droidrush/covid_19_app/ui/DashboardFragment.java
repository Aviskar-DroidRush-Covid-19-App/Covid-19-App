package com.ee.droidrush.covid_19_app.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ee.droidrush.covid_19_app.R;

import org.json.JSONArray;
import org.json.JSONObject;


public class DashboardFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        loadStates();
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    public void loadStates()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://cdn-api.co-vin.in/api/v2/admin/location/states", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray stateList = response.getJSONArray("states");
                    for (int i = 0; i < stateList.length(); i++) {
                        JSONObject st = stateList.getJSONObject(i);
                        //l.add(new Pair(st.getString("state_name"),st.getInt("state_id")));
                        Log.d("Volley","name: "+st.getString("state_name")+"state-id: "+st.getInt("state_id"));
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley",error.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);

    }
}