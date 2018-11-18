package com.mzw.appwidgetdemof;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.mzw.appwidgetdemof.tools.ConstantParameter;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by think on 2018/11/10.
 */

public class MyNetwork {


    private static final OkHttpClient okHttpClient=new OkHttpClient();
//    设置请求超时
//    okHttpClient = new OkHttpClient.Builder()
//            .connectTimeout(10, TimeUnit.SECONDS)
//        .writeTimeout(10, TimeUnit.SECONDS)
//        .readTimeout(30, TimeUnit.SECONDS)
//        .build();
//    设置缓存
//    File sdcache = getExternalCacheDir();
//    int cacheSize = 10 * 1024 * 1024;
//    okHttpClient = new OkHttpClient.Builder()
//            .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
//        .build();



    public static void putBirthday(Context mContext,String k, String v,Callback responseCallback){
        String _url = ConstantParameter.MOB_URL_PUT +
                "?key="+ConstantParameter.MOB_APP_KEY +
                "&table="+ConstantParameter.MOB_TABLE +
                "&k=" + Base64.encodeToString(k.getBytes(), Base64.URL_SAFE) + // 生日
                "&v=" + Base64.encodeToString(v.getBytes(), Base64.URL_SAFE);
        Log.i("---mzw---","_url : " + _url);
        final Request request = new Request.Builder().url(_url).build();
        okHttpClient.newCall(request).enqueue(responseCallback);
    }

    public static void getBirthday(Context mContext, String k,Callback responseCallback) {

        String _url = ConstantParameter.MOB_URL_GET +
                "?key="+ConstantParameter.MOB_APP_KEY +
                "&table="+ConstantParameter.MOB_TABLE +
                "&k=" + Base64.encodeToString(k.getBytes(), Base64.URL_SAFE);
        Log.i("---mzw---","_url : " + _url);
        final Request request = new Request.Builder().url(_url).build();
        okHttpClient.newCall(request).enqueue(responseCallback);
    }
}
