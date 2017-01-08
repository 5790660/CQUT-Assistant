package com.xybst.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xybst.activity.R;
import com.xybst.ui.adapter.ArticleListAdapter;
import com.xybst.bean.NewsItem;
import com.xybst.util.Home;
import com.xybst.presenter.LoadItemPresenter;
import com.xybst.ui.ILoadItemView;
import com.xybst.ui.activity.ArticleContentActivity;
import com.xybst.util.NewsType;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Use MVP design pattern
 */
public class ArticleListFragment extends Fragment implements
        ArticleListAdapter.OnArticleListInteractionListener, ILoadItemView {

    private static final String ARG_TYPE = "type";

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private LinearLayoutManager mLayoutManager;

    private ArticleListAdapter adapter = new ArticleListAdapter(this);

    private LoadItemPresenter presenter = new LoadItemPresenter(this);

    private NewsType newsType;

    private boolean isLoading = false;

    public static ArticleListFragment newInstance(NewsType type) {
        ArticleListFragment fragment = new ArticleListFragment();
        Bundle bundle  = new Bundle();
        bundle.putSerializable(ARG_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            newsType = (NewsType) getArguments().getSerializable(ARG_TYPE);
        System.out.println(newsType.name());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_articlelist, container, false);
        ButterKnife.bind(this, view);

        mRefreshLayout.setColorSchemeResources(R.color.colorTheme);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadFromWeb(getContext(), 1);
            }
        });

        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(mLayoutManager);

        presenter.loadFromCache(getContext());

        return view;
    }

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
                    presenter.loadFromWeb(getContext(), adapter.getItemCount() / Home.PAGE_SIZE + 1);
                }
            }
        });
    }

    @Override
    public NewsType getNewsType() {
        return newsType;
    }

    @Override
    public void updateData(List<NewsItem> items) {
        adapter.updateData(items);
        isLoading = false;
    }

    @Override
    public void showLoading() {
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void hideLoading() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoadFailed() {
        Toast.makeText(getContext(), "请检查网络连接", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onArticleListInteraction(NewsItem item) {
        Intent intent = new Intent(getActivity(), ArticleContentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("article", item);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
