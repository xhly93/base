
package com.ssj.user.Mode.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.ssj.user.Utils.SharedPreferenceUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class HttpUtils {
    private static final String TAG = "HttpUtils";
    public static final int METHOD_GET = 1;
    public static final int METHOD_POST = 2;
    public static final int CONNCT_TIME = 3000;

    public static InputStream getInputStreamGet(String uri, List<NameValuePair> list) throws Exception {
        HttpEntity entity = null;
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNCT_TIME);
        HttpUriRequest request = null;
        StringBuilder sb = new StringBuilder(uri);
        if (list != null && !list.isEmpty()) {
            for (NameValuePair p : list) {
                sb.append(p.getName()).append("=").append(p.getValue()).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        request = new HttpGet(sb.toString());
        request.setHeader("token", SharedPreferenceUtil.getToken());
        HttpResponse response = client.execute(request);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            entity = response.getEntity();
        } else {
            Log.d(TAG, "StatusCode:" + response.getStatusLine().getStatusCode());
        }
        return entity.getContent();
    }

    public static InputStream getInputStreamPost(String uri, List<NameValuePair> list) throws Exception {
        JSONObject ClientKey = new JSONObject();
        if (list != null && !list.isEmpty()) {
            for (NameValuePair p : list) {
                ClientKey.put(p.getName(), p.getValue());
            }
        }
        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(CONNCT_TIME);
        // 设置允许输出
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        // 设置User-Agent: Fiddler
        conn.setRequestProperty("Accept", "application/json");
        // 设置contentType
        conn.setRequestProperty("Content-Type", "application/json");
        // 设置token
        conn.setRequestProperty("token", SharedPreferenceUtil.getToken());
        OutputStream os = conn.getOutputStream();
        os.write(String.valueOf(ClientKey).getBytes());
        os.close();
        if (conn.getResponseCode() == 200) {
            InputStream in = conn.getInputStream();
            //conn.getInputStream().close();
            return in;
        } else {
            try {
                conn.getInputStream().close();
            } catch (Exception e) {
            }
            Log.d(TAG, "StatusCode:" + conn.getResponseCode());
            return null;
        }
    }

    public static long getLength(HttpEntity entity) {
        long len = 0;
        if (entity != null) {
            len = entity.getContentLength();
        }
        return len;
    }

    public static InputStream getStream(HttpEntity entity) throws Exception {

        InputStream in = null;
        if (entity != null) {
            in = entity.getContent();
        }
        return in;
    }

    public static boolean isNetWorkReadly(Context context) {
        boolean ret = true;
        try {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager == null) {
                ret = false;
            } else {
                NetworkInfo networkinfo = manager.getActiveNetworkInfo();
                if (networkinfo == null || !networkinfo.isAvailable() || !networkinfo.isConnectedOrConnecting()) {
                    ret = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = false;
        }
        return ret;
    }
}
