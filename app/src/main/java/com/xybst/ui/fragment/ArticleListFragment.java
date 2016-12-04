package com.xybst.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xybst.activity.R;
import com.xybst.adapter.ArticleListAdapter;
import com.xybst.bean.ArticlesListItem;
import com.xybst.dao.ArticleListDAO;
import com.xybst.net.Home;
import com.xybst.net.HttpHelper;
import com.xybst.net.HttpUtil;
import com.xybst.ui.activity.ArticleContentActivity;
import com.xybst.utils.Info;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class ArticleListFragment extends Fragment implements ArticleListAdapter.OnArticleListInteractionListener {

    private final static String ARG_NEWS_MODULE= "news_module";
    /**
    *综合新闻
    * */
    public static final String NEWS_COLLEGE = "collegeNews";
    /**
     *学校通知
     * */
    public static final String NEWS_COLLEGE_NOTIFICATION ="collegeNoti";
    /**
     *部门通知
     * */
    public static final String NEWS_DEPARTMENT_NOTIFICATION = "departNoti";
    /**
     *学生通知
     * */
    public static final String NEWS_STUDENT_NOTIFICATION = "studentNoti";
    /**
     *招生就业
     * */
    public static final String NEWS_RECRUITMENT_EMPLOYMENT = "studentRecruit";

    public static final int TASK_REFRESH = 0;

    public static final int TASK_LOAD_MORE = 1;

    private SwipeRefreshLayout.OnRefreshListener sListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            new BackgroundTask(TASK_REFRESH).execute(1);
        }
    };

    private Info info;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private ArticleListAdapter articleListAdapter;
    private RecyclerView recyclerView;
    private String module;

    private boolean isLoading = false;

    public ArticleListFragment() {
    }

    public static ArticleListFragment newInstance(String module) {
        ArticleListFragment fragment = new ArticleListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NEWS_MODULE, module);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            module = getArguments().getString(ARG_NEWS_MODULE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_articlelist, container, false);
        info = (Info) getActivity().getApplication();
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mSwipeRefreshLayout.setOnRefreshListener(sListener);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorTheme);

        articleListAdapter = new ArticleListAdapter(this);
        if (info.getArticleListContainer().containsKey(module)) {
            articleListAdapter.updateData(info.getArticleListContainer().get(module));
        } else {
            articleListAdapter.updateData(getItemsFormDB(getContext(), module));
            if (HttpUtil.isNetAvailable(getContext())) {
                mSwipeRefreshLayout.post(runnable);
                sListener.onRefresh();
            }
        }
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setAdapter(articleListAdapter);
        recyclerView.setLayoutManager(mLayoutManager);
        return view;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItems = mLayoutManager.getChildCount();
                int totalItems = mLayoutManager.getItemCount();
                int passItems = mLayoutManager.findFirstVisibleItemPosition();
                if(visibleItems + passItems >= totalItems && !isLoading && totalItems != 0) {
                    isLoading = true;
                    new BackgroundTask(TASK_LOAD_MORE).execute(articleListAdapter.getItemCount() / Home.PAGE_SIZE + 1);
                }
            }
        });
    }

    private class BackgroundTask extends AsyncTask<Integer, Void, List<ArticlesListItem>> {

        int type;

        public BackgroundTask(int type) {
            this.type = type;
        }

        @Override
        protected List<ArticlesListItem> doInBackground(Integer... params) {
            if (HttpUtil.isNetAvailable(getContext())) {
                return getItemsFromWeb(module, params[0]);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<ArticlesListItem> items) {
            super.onPostExecute(items);
            if (items == null || items.size() == 0) {
                Toast.makeText(ArticleListFragment.this.getContext(), "请检查网络连接", Toast.LENGTH_SHORT).show();
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            } else if(type == TASK_REFRESH){
                refreshComplete(items);
            } else {
                loadMoreComplete(items);
            }
        }
    }

    //完成刷新
    public void refreshComplete(List<ArticlesListItem> items) {
        articleListAdapter.updateData(items);
        info.getArticleListContainer().put(module, items);
        new ArticleListDAO(getContext()).addArticleList(module, items);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    //加载更多
    public void loadMoreComplete(List<ArticlesListItem> items) {
        articleListAdapter.addMoreData(items);
        isLoading = false;
    }

    @Override
    public void onArticleListInteraction(ArticlesListItem item) {
        Intent intent = new Intent(getActivity(), ArticleContentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("article", item);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    public List<ArticlesListItem> getItemsFormDB(Context context, String module) {
        List<ArticlesListItem> items = new ArticleListDAO(context).getArticleListItems(module);
        info.getArticleListContainer().put(module, items);
        return items;
    }

    public List<ArticlesListItem> getItemsFromWeb(final String module, final int pageNo) {
        String stringUrl = null ;
        final List<ArticlesListItem> items = new ArrayList<>();

        if (module.equals(NEWS_COLLEGE)) {
            stringUrl = Home.NEWSMODULE_COLLIGATE;
        } else if (module.equals(NEWS_COLLEGE_NOTIFICATION)) {
            stringUrl =  Home.NEWSMODULE_NOTIFICATION_COLLAGE;
        } else if (module.equals(NEWS_DEPARTMENT_NOTIFICATION)) {
            stringUrl = Home.NEWSMODULE_NOTIFICATION_DEPARTMENT;
        } else if (module.equals(NEWS_STUDENT_NOTIFICATION)) {
            stringUrl = Home.NEWSMODULE_NOTIFICATION_STUDENT;
        } else if (module.equals(NEWS_RECRUITMENT_EMPLOYMENT)) {
            stringUrl = Home.NEWSMODULE_RECRUITMENT_EMPLOYMENT;
        }

        RequestParams requestParams = new RequestParams();
        requestParams.put("pageNo", pageNo);

        HttpHelper.get(Home.HOST_NEWS_HOMEPAGE + stringUrl, requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                InputStream inputStream = new ByteArrayInputStream(responseBody);
                items.addAll(parse(inputStream, module));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
        return items;
    }


    public static List<ArticlesListItem> parse(InputStream input, String module) {
        List<ArticlesListItem> articlesList = new ArrayList<>();
        if (input == null) return articlesList;

        Pattern pLink = Pattern.compile("(?<=<a target=\"_blank\" href=\").*(?=\" title=\")");
        Pattern pTitle = Pattern.compile("(?<=\" title=\").*(?=\">)");
        Pattern pPublisher = Pattern.compile("(?<=<span>).*(?=[<]/span[>][<]/a[>] [<]span class=\"date\"[>])");
        Pattern pPublishTime = Pattern.compile("(?<=class=\"date\">).*(?=</span>)");

        Matcher m;

        String link = null;
        String title = null;
        String puber = null;
        String time = null;

        Scanner scanner = new Scanner(input);
        String str = null;
        while (scanner.hasNextLine()) {
            str = scanner.nextLine();
            m = pLink.matcher(str);
            if (m.find()) {
                link = m.group();
            }

            m = pTitle.matcher(str);
            if (m.find()) {
                title = m.group();
                if (title.contains("&quot;")) {
                    title = title.replace("&quot;", "\"");
                }
            }

            m = pPublisher.matcher(str);
            if (m.find()) {
                puber = m.group();
            }

            m = pPublishTime.matcher(str);
            if (m.find()) {
                time = m.group();
            }
            if (link != null && title != null && puber != null) {
                articlesList.add(new ArticlesListItem(0, Home.HOST_NEWS_HOMEPAGE + link, module, puber, time, title));
                link = null;
                title = null;
                puber = null;
            }
        }
        for (ArticlesListItem item : articlesList) {
            System.out.println(item.toString());
        }
        scanner.close();
        return articlesList;
    }

    @Override
    public void onStop() {
        super.onStop();
        mSwipeRefreshLayout.removeCallbacks(runnable);
    }

    private class mArticleListSwipeRefreshLayout extends SwipeRefreshLayout{

        public mArticleListSwipeRefreshLayout(Context context) {
            super(context);
        }

        @Override
        public boolean canChildScrollUp() {
            if(getVisibility() == VISIBLE) {
                return ViewCompat.canScrollVertically(getView(), -1);
            } else {
                return false;
            }
        }
    }
}
