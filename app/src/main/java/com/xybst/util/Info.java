package com.xybst.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.xybst.bean.Course;
import com.xybst.bean.Grade;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 创宇 on 2016/1/10.
 */
public class Info extends Application{

    public static Info instance;

    public static Info getInstance() {
        return instance;
    }

    public Info() {
        instance = this;
    }

    public static final String PREFERENCES_USER_INFO = "userInfo";
    public static final String PREFERENCES_SETTING = "setting";

    private int currentWeek;
    private String studentName;
    private String studentId;
    private String password;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences share = getSharedPreferences(PREFERENCES_USER_INFO, Context.MODE_PRIVATE);
        studentName = share.getString("studentName", "未登录");
        studentId = share.getString("studentId", null);
        password = share.getString("password", null);
        currentWeek =  Integer.valueOf(getSharedPreferences(PREFERENCES_SETTING, Context.MODE_PRIVATE).getString("current_week", "1"));
    }

    public int getCurrentWeek() {
        return currentWeek;
    }

    public void setCurrentWeek(int currentWeek) {
        this.currentWeek = currentWeek;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    @Override
    public String toString() {
        return "Info{" +
                "currentWeek=" + currentWeek +
                ", studentName='" + studentName + '\'' +
                ", studentId='" + studentId + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
