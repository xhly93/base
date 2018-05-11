package com.ssj.user.Mode.network;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ssj.user.Mode.Data.TeacherReturnLoginData;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by 17258 on 2018/4/10.
 */

public class MyGsonConverterFactory extends Converter.Factory {

    private static final String TAG = "MyGsonConverterFactory";

    public static MyGsonConverterFactory create() {
        return new MyGsonConverterFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new JsonResponseBodyConverter<JsonObject>();
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }

    final class JsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

        JsonResponseBodyConverter() {

        }

        @Override
        public T convert(ResponseBody value) throws IOException {

            TeacherReturnLoginData data = new TeacherReturnLoginData();

            try {
                String string = value.string();
                Log.i(TAG, "string = " + string);
                com.google.gson.JsonParser jsonParser = new com.google.gson.JsonParser();
                JsonElement parse = jsonParser.parse(string);
                Log.i(TAG, "parse = " + parse.toString());
                if (null != parse && parse.isJsonObject()) {
                    JsonObject res = jsonParser.parse(string).getAsJsonObject();
                    Log.i(TAG, "res = " + res.toString());
                    String code = res.get("code").getAsString();
                    JsonElement jsonElement = res.get("data");
                    JsonObject jsonObject = null;
                    if (null != jsonElement && !jsonElement.isJsonNull() && jsonElement.isJsonObject()) {
                        jsonObject = jsonElement.getAsJsonObject();
                    }
                    String msg = res.get("msg").getAsString();
                    String status = res.get("status").getAsString();
                    Log.i(TAG, "code = " + code);
                    Log.i(TAG, "jsonObject = " + jsonObject);
                    Log.i(TAG, "msg = " + msg);
                    Log.i(TAG, "status = " + status);
                    data.setStatus(status);
                    data.setData(jsonObject);
                    data.setMsg(msg);
                    data.setCode(code);
                }
                return (T) data;
            } catch (Exception e) {
                Log.i(TAG, "parseData e = " + e.getMessage().toString());
                return (T)data;
            }
        }
    }

}
