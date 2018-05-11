package com.ssj.user.Mode.interfac;



import com.ssj.user.Mode.Data.TeacherReturnLoginData;
import com.ssj.user.Utils.Contants;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 网络请求函数集
 * Created by 17258 on 2018/4/3.
 */

public interface ApiServices {


    /**
     * 登陆
     */
    @POST(Contants.NetWorkUrl.USER_LOGON)
    Observable<TeacherReturnLoginData> login(@Body RequestBody requestBody);

    @GET()
    Observable<ResponseBody> get(@Url String url);



    /**
     * 下载文件
     */
    @Streaming
    @GET()
    Observable<ResponseBody> downLoadFile(@Url String url);


}
