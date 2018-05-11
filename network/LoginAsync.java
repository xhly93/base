package com.ssj.user.Mode.network;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ssj.user.Mode.Data.ServiceReturnLoginData;
import com.ssj.user.Mode.interfac.AsyncLoginCallBack;

import org.apache.http.NameValuePair;

import java.io.InputStream;
import java.util.List;

/**
 * Created by 王矩龙 on 2018/3/5.
 */

public class LoginAsync extends AsyncTask<List<NameValuePair>, Void, ServiceReturnLoginData> {
    private static final String TAG = "LoginAsync";
    private String mUrl;
    private AsyncLoginCallBack mAsyncCallBack;
    private int HTTP_METHOD;

    public LoginAsync(String url, int method, AsyncLoginCallBack callback) {
        this.mUrl = url.trim();
        this.HTTP_METHOD = method;
        this.mAsyncCallBack = callback;
    }

    @Override
    protected ServiceReturnLoginData doInBackground(List<NameValuePair>... params) {
        Log.d(TAG, "request:" + mUrl);
        try {
            List<NameValuePair> list = null;
            if (params.length > 0) {
                list = params[0];
                Log.d(TAG, params[0].toString());
            }
            InputStream input = null;
            switch (HTTP_METHOD) {
                case HttpUtils.METHOD_GET:
                    input = HttpUtils.getInputStreamGet(mUrl, list);
                    break;
                case HttpUtils.METHOD_POST:
                    input = HttpUtils.getInputStreamPost(mUrl, list);
                    break;
            }
            if (null == input) {
                Log.d(TAG, "data is null");
                return null;
            }

//            ServiceReturnLoginData data = gson.fromJson(JsonParser.formatStreamToString(input), ServiceReturnLoginData.class);
            ServiceReturnLoginData data = parseData(input);
            if (data != null) {
                Log.d(TAG, data.toString());
            }
            return data;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private ServiceReturnLoginData parseData(InputStream input) {
        Gson gson = new Gson();
        ServiceReturnLoginData data = new ServiceReturnLoginData();
        try {
            String string = JsonParser.formatStreamToString(input);
            JsonObject jsonObject = gson.fromJson(string, JsonObject.class);
            data.setStatus(jsonObject.get("status").getAsInt());
            data.setMessage(jsonObject.get("message").getAsString());
            data.setSuccess(jsonObject.get("success").getAsBoolean());
            JsonElement jsonElement = jsonObject.get("jsonObj");
            if (null != jsonElement && !jsonElement.isJsonNull()) {
                data.setJsonObj(jsonObject.get("jsonObj").getAsJsonObject());
            } else {
                data.setJsonObj(null);
            }

        } catch (Exception e) {
            Log.i(TAG, "parseData e = " + e.getMessage().toString());
        }

        return data;

    }

    @Override
    protected void onPostExecute(ServiceReturnLoginData data) {
        super.onPostExecute(data);
        mAsyncCallBack.netBack(data);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}
