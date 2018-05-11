package com.ssj.user.Mode.network;


import com.ssj.user.Mode.interfac.ApiServices;
import com.ssj.user.Utils.Contants;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * 网络请求Retrofit
 * Created by 17258 on 2018/4/3.
 */

public class SSJRetrofit {

    private static Retrofit mRetrofit;
    private static OkHttpClient mOkHttpClient;

    private static class SingletonHolder {
        public static SSJRetrofit instance = new SSJRetrofit();
    }

    private SSJRetrofit() {
        initRetrofit();
    }

    public static SSJRetrofit getInstance() {
        return SingletonHolder.instance;
    }


    private OkHttpClient genericClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        mOkHttpClient = new OkHttpClient.Builder()
//                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
//                .writeTimeout(10, TimeUnit.SECONDS)//设置写的超时时间
//                .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new HeadInterceptor()).build();
        return mOkHttpClient;
    }

    private void initRetrofit() {
        if (null == mRetrofit) {
            if (null == mOkHttpClient) {
                mOkHttpClient = genericClient();
            }
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(Contants.NetWorkUrl.BASE_URL_PARENT)
                    .addConverterFactory(MyGsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(mOkHttpClient)
                    .build();
        }
    }

    public ApiServices getApi() {
       return mRetrofit.create(ApiServices.class);
    }

    public static ObservableTransformer schedulersTransformer() {
        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


}
