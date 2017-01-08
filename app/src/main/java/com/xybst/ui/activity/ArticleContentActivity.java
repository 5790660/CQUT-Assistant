package com.xybst.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.xybst.activity.R;
import com.xybst.bean.NewsItem;
import com.xybst.persistence.FavoriteDAO;
import com.xybst.util.HtmlUtils;
import com.xybst.util.HttpUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class ArticleContentActivity extends AppCompatActivity {

    private View.OnClickListener navListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ArticleContentActivity.this.finish();
        }
    };

    WebView webView;
    boolean favoriteStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow_back);
        toolbar.setNavigationOnClickListener(navListener);

        Intent intent = getIntent();
        final NewsItem item = (NewsItem) intent.getSerializableExtra("article");
        getSupportActionBar().setTitle(item.getTitle());

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final FavoriteDAO dao =new FavoriteDAO(this);
        if(dao.isFavorite(item.getLink())) {
            fab.setImageResource(android.R.drawable.star_big_on);
            favoriteStatus = true;
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favoriteStatus) {
                    fab.setImageResource(android.R.drawable.star_big_off);
                    Toast.makeText(ArticleContentActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                    dao.unFavorite(item.getLink());
                    favoriteStatus = false;
                } else {
                    fab.setImageResource(android.R.drawable.star_big_on);
                    Toast.makeText(ArticleContentActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                    dao.addFavoriteArticle(item);
                    favoriteStatus = true;
                }
            }
        });

        webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setTextZoom(180);

        loadArticleFromWeb(item.getLink());
    }

    public void loadArticleFromWeb(final String stringUrl) {
        HttpUtils.get(stringUrl, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("conn", "success");

                InputStream input = new ByteArrayInputStream(responseBody);
                StringBuilder str = new StringBuilder();
                str.append("<html><head><meta content=\"text/html; charset=utf-8\" http-equiv=content-type></head><body>");
                str.append(HtmlUtils.parseArticle(input));
                str.append("</body></html>");
                webView.getSettings().setDefaultTextEncodingName("utf-8");
                webView.setInitialScale(180);
                webView.loadDataWithBaseURL("", str.toString(), "text/html", "utf-8", "");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("conn", "failure");
            }
        });
    }
}
