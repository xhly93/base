package com.ssj.user.Mode.network;


import android.util.Log;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * header拦截器
 * Created by 17258 on 2018/4/3.
 */

public class HeadInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
//        String token = SharedPreferenceUtil.getToken();
//        Log.i("ssss","token = "+token);
        Request request = chain.request()
                .newBuilder()
//                .addHeader("token", token)
//                .addHeader("Accept", "application/json")
//                .addHeader("Content-Type", "application/json")
                .build();
        return chain.proceed(request);
    }

}
