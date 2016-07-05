package com.xybst.net;

import android.os.Looper;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

/**
 * Created by 创宇 on 2015/12/16.
 */
public class HttpHelper {

    public static AsyncHttpClient syncHttpClient = new SyncHttpClient();
    public static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    static {
        getClient().setTimeout(5000);
    }

    //获取String对象(无参数)
    public static void get(String urlString, AsyncHttpResponseHandler res) {
        getClient().get(urlString, res);
    }

    //获取String对象(有参数)
    public static void get(String uriString, RequestParams params, AsyncHttpResponseHandler res) {
        getClient().get(uriString, params, res);
    }

    //获取json对象或数组
    public static void get(String urlString,RequestParams params,JsonHttpResponseHandler res)   //带参数，获取json对象或者数组
    {
        getClient().get(urlString, params,res);
    }

    public static AsyncHttpClient getClient(){
        if(Looper.myLooper() == null)
            return syncHttpClient;
        else
            return asyncHttpClient;
    }
}