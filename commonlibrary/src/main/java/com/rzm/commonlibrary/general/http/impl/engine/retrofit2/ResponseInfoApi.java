package com.rzm.commonlibrary.general.http.impl.engine.retrofit2;

import android.database.Observable;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by rzm on 2017/11/2.
 */

public interface ResponseInfoApi {

    @POST("http://www.baidu.com")
    @FormUrlEncoded
    Call<Response> login(@Field("mobile") String mobile);

    @GET("http://www.baidu.com")
    Call<Response> getMessage(@Query("token") String token);

    @FormUrlEncoded
    @POST()
    Call<String> post(@Url String url, @FieldMap Map<String, Object> params);

    @GET()
    Call<String> get(@Url String url, @QueryMap Map<String, Object> params);

    @FormUrlEncoded
    @POST()
    Observable<String> Obpost(@Url String url, @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @PUT
    Observable<String> Obput(@Url String url, @FieldMap Map<String, Object> params);

    @Streaming
    @GET()
    Observable<ResponseBody> Obdownload(@Url String url, @QueryMap Map<String, Object> params);

    @Streaming
    @GET()
    Call<ResponseBody> download(@Url String url, @QueryMap Map<String, Object> params);

}
