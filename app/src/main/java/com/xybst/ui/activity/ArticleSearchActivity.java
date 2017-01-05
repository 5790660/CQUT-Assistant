package com.xybst.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xybst.activity.R;
import com.xybst.bean.NewsItem;
import com.xybst.ui.adapter.SearchListAdapter;
import com.xybst.util.Home;
import com.xybst.util.HttpUtils;
import com.xybst.util.HtmlUtils;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ArticleSearchActivity extends AppCompatActivity {

    private List<NewsItem> items = new ArrayList<>();
    private View.OnClickListener navListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ArticleSearchActivity.this.finish();
        }
    };
    private ProgressDialog progressDialog;
    private ListView listView;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow_back);
        toolbar.setNavigationOnClickListener(navListener);

        listView = (ListView) findViewById(R.id.listView);
        editText = (EditText) findViewById(R.id.editSearch);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                    search(editText.getText().toString());
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ArticleSearchActivity.this, ArticleContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("article", items.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void search(String keywords) {
        progressDialog = ProgressDialog.show(ArticleSearchActivity.this, null, "搜索中……");
        RequestParams params = new RequestParams();
        params.put("keyInfo", keywords + "_1_0_0_0_0");
        search(params);
        params.put("keyInfo", keywords + "_2_0_0_0_0");
        search(params);
    }

    private void search(RequestParams params) {
        HttpUtils.get(Home.HOST_SEARCHARTICLE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                List<NewsItem> list = HtmlUtils.parseNewsItems(new ByteArrayInputStream(responseBody), null);
                if (items.isEmpty())
                    items = list;
                else {
                    items.addAll(list);
                    sortByTime();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });
    }

    private void sortByTime(){
        Collections.sort(items, new Comparator<NewsItem>() {
            public int compare(NewsItem arg0, NewsItem arg1) {
                return -arg0.getTime().compareTo(arg1.getTime());
            }
        });
        listView.setAdapter(new SearchListAdapter(items));
        progressDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_article_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search){
            search(editText.getText().toString());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
