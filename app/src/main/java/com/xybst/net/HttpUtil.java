package com.xybst.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.xybst.bean.ArticlesListItem;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.utils.URLEncodedUtils;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by 创宇 on 2016/4/28.
 */
public class HttpUtil {

    public static final String BASE_URL= "http://www.cqut.pw/api";

    private static final int TIMEOUT_IN_MILLIONS = 5000;

    /**
     * 当前是否有网络连接
     * @return
     */
    public static boolean isNetAvailable(Context context){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

   public static InputStream getStream(String baseUrl, List<NameValuePair> pairList) {
        try {
            String param = URLEncodedUtils.format(pairList, HTTP.UTF_8);
            HttpGet httpGet = new HttpGet(baseUrl + "?" + param);//将URL与参数拼接
            HttpClientBuilder builder = HttpClientBuilder.create();
            builder.setConnectionTimeToLive(TIMEOUT_IN_MILLIONS, TimeUnit.MILLISECONDS);

            CloseableHttpClient client = builder.build();
            HttpResponse response = client.execute(httpGet);
            if (null == response) {
                return null;
            }
            HttpEntity httpEntity = response.getEntity();
            return httpEntity.getContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String get(String baseUrl, List<NameValuePair> pairList) {
        try {
            String param = URLEncodedUtils.format(pairList, HTTP.UTF_8);
            HttpGet httpGet = new HttpGet(baseUrl + "?" + param);//将URL与参数拼接
            HttpClientBuilder builder = HttpClientBuilder.create();
            builder.setConnectionTimeToLive(TIMEOUT_IN_MILLIONS, TimeUnit.MILLISECONDS);

            CloseableHttpClient client = builder.build();
            HttpResponse response = client.execute(httpGet);

//            HttpClient httpClient = new DefaultHttpClient();
//            HttpResponse response = httpClient.execute(httpGet);
            return showResponseResult(response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String post(String baseUrl, List<NameValuePair> pairList) {
        try {
            HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList, HTTP.UTF_8);
            HttpPost httpPost = new HttpPost(baseUrl);
            httpPost.setEntity(requestHttpEntity);
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(httpPost);
            return showResponseResult(response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String showResponseResult(HttpResponse response)
    {
        if (null == response) {
            return null;
        }
        HttpEntity httpEntity = response.getEntity();
        try {
            InputStream inputStream = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            String result = "";
            String line = "";
            while (null != (line = reader.readLine())) {
                result += line;
            }
            System.out.println(result);
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
