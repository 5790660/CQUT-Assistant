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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.xybst.ui.activity.ArticleSearchActivity;
import com.xybst.activity.R;

/**
 * 新闻与通知 Fragment
 * */
public class NewsFragment extends Fragment{

    private static final String ARG_FM = "ARG_FM";
    private ViewPager mViewPager;

    public static Fragment getInstance(String string) {
        NewsFragment fragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putString( ARG_FM, string);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.news);
        View view = inflater.inflate(R.layout.fragment_news, container, false);

       mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        mViewPager.setAdapter(new NewsFragmentAdapter(getActivity().getSupportFragmentManager()));
   /*      mViewPager.setOffscreenPageLimit(5);
        mViewPager.addOnPageChangeListener(this);

        mTabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        mTabLayout.setTabMode(MODE_SCROLLABLE);
        mTabLayout.setupWithViewPager(mViewPager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTabLayout.setElevation(20);
        }
        mTabLayout.setTabTextColors(getResources().getColor(R.color.gray), getResources().getColor(R.color.white));
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
        mTabLayout.setBackgroundColor(getResources().getColor(R.color.colorTheme));*/
//        mTabLayout.setTabsFromPagerAdapter(mViewPager);

        //tab相关设置
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view.findViewById(R.id.pagerSlidingStrip);
        tabs.setBackgroundColor(getResources().getColor(R.color.colorTheme));
        tabs.setTextColorResource(android.R.color.white);
        tabs.setTextSize(sp2px(16));
        tabs.setIndicatorColorResource(android.R.color.white);
        tabs.setDividerColorResource(R.color.colorTheme);
        tabs.setIndicatorHeight(6);
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP)
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
    public class NewsFragmentAdapter extends FragmentStatePagerAdapter {

        private String[] titles = {"校内新闻", "校内通知", "部门通知", "学生通知", "招生就业"};

        public NewsFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    Log.i("create new fragment", "1");
                    return ArticleListFragment.newInstance(ArticleListFragment.NEWS_COLLEGE);
                }
                case 1: {
                    Log.i("create new fragment", "2");
                    return ArticleListFragment.newInstance(ArticleListFragment.NEWS_COLLEGE_NOTIFICATION);
                }
                case 2: {
                    Log.i("create new fragment", "3");
                    return ArticleListFragment.newInstance(ArticleListFragment.NEWS_DEPARTMENT_NOTIFICATION);
                }
                case 3: {
                    Log.i("create new fragment", "4");
                    return ArticleListFragment.newInstance(ArticleListFragment.NEWS_STUDENT_NOTIFICATION);
                }
                case 4: {
                    Log.i("create new fragment", "5");
                    return ArticleListFragment.newInstance(ArticleListFragment.NEWS_RECRUITMENT_EMPLOYMENT);
                }
            }
            return null;
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
