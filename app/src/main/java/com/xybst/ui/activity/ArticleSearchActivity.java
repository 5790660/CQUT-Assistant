package com.xybst.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.xybst.activity.R;
import com.xybst.adapter.SearchListAdapter;
import com.xybst.bean.ArticlesListItem;
import com.xybst.net.Home;
import com.xybst.net.HttpUtil;
import com.xybst.ui.fragment.ArticleListFragment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class ArticleSearchActivity extends AppCompatActivity {

    private List<ArticlesListItem> items = new ArrayList<>();
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
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    new ArticleSearchTask().execute(editText.getText().toString());
                    progressDialog = ProgressDialog.show(ArticleSearchActivity.this, null, "搜索中……");
                }
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

    private class ArticleSearchTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            items.clear();
            getItemsFromWeb(params[0] + "_1_0_0_0_0");
            getItemsFromWeb(params[0] + "_2_0_0_0_0");
            return null;
        }

        @Override
        protected void onPostExecute(Void params) {
            super.onPostExecute(params);
            progressDialog.dismiss();
            //按照时间排序
            Collections.sort(items, new Comparator<ArticlesListItem>() {
                public int compare(ArticlesListItem arg0, ArticlesListItem arg1) {
                    return -arg0.getTime().compareTo(arg1.getTime());
                }
            });
            listView.setAdapter(new SearchListAdapter(ArticleSearchActivity.this, items));
        }
    }

    public void getItemsFromWeb(String url) {
        List<NameValuePair> pairList = new ArrayList<>();
        pairList.add(new BasicNameValuePair("keyInfo", url));
        InputStream inputStream = HttpUtil.getStream(Home.HOST_SEARCHARTICLE, pairList);
        items.addAll(ArticleListFragment.parse(inputStream, "news"));
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
            new ArticleSearchTask().execute(editText.getText().toString());
            progressDialog = ProgressDialog.show(ArticleSearchActivity.this, null, "搜索中……");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
