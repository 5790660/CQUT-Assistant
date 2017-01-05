package com.xybst.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.xybst.activity.R;
import com.xybst.ui.view.LoginDialog;
import com.xybst.ui.fragment.FavoriteFragment;
import com.xybst.ui.fragment.GradeFragment;
import com.xybst.ui.fragment.NewsFragment;
import com.xybst.ui.fragment.TimetableFragment;
import com.xybst.util.DataCleanManager;
import com.xybst.util.Info;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void initView() {
        //Set Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set DrawerLayout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar
                , R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Set navigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_news);
        navigationView.setNavigationItemSelectedListener(this);
        View view = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null, false);
        navigationView.addHeaderView(view);

        if (Info.getInstance().getStudentId() != null) {
            navigationView.getMenu().setGroupVisible(R.id.group_login, false);
            ((TextView)view.findViewById(R.id.tvStudentName)).setText(Info.getInstance().getStudentName());;
        }

        switchFragment(new NewsFragment());
    }

    private void switchFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_login:
                LoginDialog.newInstance().show(getSupportFragmentManager(), null);
                return false;
            case R.id.nav_news:
                switchFragment(new NewsFragment());
                break;
            case R.id.nav_grade:
                if (Info.getInstance().getStudentId() != null) {
                    switchFragment(new GradeFragment());
                } else {
                    LoginDialog.newInstance().show(getSupportFragmentManager(), null);
                    return false;
                }
                break;
            case R.id.nav_timetable:
                if (Info.getInstance().getStudentId() != null) {
                    switchFragment(new TimetableFragment());
                } else {
                    LoginDialog.newInstance().show(getSupportFragmentManager(), null);
                    return false;
                }
                break;
            case R.id.nav_favorite:
                switchFragment(new FavoriteFragment());
                break;
            case R.id.nav_setting:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.nav_share:
                showShare("发现一个有趣的APP，你也来下载试试吧！ "+getString(R.string.app_homepage));
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_logout);
        if (Info.getInstance().getStudentId() == null) item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_logout) {
            DataCleanManager.clean(this);
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setCheckedItem(R.id.nav_news);
            recreate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 分享
     * @param text {@link String} 内容
     */
    protected void showShare(String text) {
        ShareSDK.initSDK(this);

        String appHomePage = getString(R.string.app_homepage);

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.app_name));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(appHomePage);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(text);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(appHomePage);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(appHomePage);

        // 启动分享GUI
        oks.show(this);
    }
}
