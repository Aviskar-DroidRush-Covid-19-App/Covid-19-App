package com.ee.droidrush.covid_19_app;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.HashMap;
import java.util.Map;

public class VolleyFileDownloadRequest extends Request<byte[]> {
    private final Response.Listener<byte[]> mListener;
    private Map<String,String> mParams;
    public Map<String,String> responseHeaders;

    public VolleyFileDownloadRequest(int method, String url,Response.Listener<byte[]> listener, @Nullable Response.ErrorListener errorListener, HashMap<String,String> params) {
        super(method, url, errorListener);
        setShouldCache(false);
        mParams=params;
        mListener=listener;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError
    {
        return mParams;
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        responseHeaders=response.headers;
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(byte[] response) {
        mListener.onResponse(response);

    }
}
