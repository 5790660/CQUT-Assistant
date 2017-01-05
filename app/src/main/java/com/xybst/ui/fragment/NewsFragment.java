package com.xybst.ui.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.xybst.ui.activity.ArticleSearchActivity;
import com.xybst.activity.R;
import com.xybst.util.NewsType;

/**
 * 新闻与通知 Fragment
 * */
public class NewsFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.news);
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        mViewPager.setAdapter(new NewsFragmentAdapter(getActivity().getSupportFragmentManager()));

        //tab相关设置
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view.findViewById(R.id.pagerSlidingStrip);
        tabs.setBackgroundColor(getResources().getColor(R.color.colorTheme));
        tabs.setTextColorResource(android.R.color.white);
        tabs.setTextSize(sp2px(16));
        tabs.setIndicatorColorResource(android.R.color.white);
        tabs.setDividerColorResource(R.color.colorTheme);
        tabs.setIndicatorHeight(6);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
            tabs.setElevation(20);
        tabs.setViewPager(mViewPager);
        return view;
    }

    public int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 新闻与通知 Fragment适配器
    * */
    private class NewsFragmentAdapter extends FragmentStatePagerAdapter {

        private String[] titles = {"校内新闻", "校内通知", "部门通知", "学生通知", "招生就业"};

        private NewsFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ArticleListFragment.newInstance(NewsType.getTypeByIndex(position));
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_article_search, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_search){
            Intent intent = new Intent(getActivity(), ArticleSearchActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
