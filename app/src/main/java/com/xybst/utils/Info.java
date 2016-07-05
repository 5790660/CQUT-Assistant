package com.xybst.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.xybst.bean.ArticlesListItem;
import com.xybst.bean.Course;
import com.xybst.bean.Grade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 创宇 on 2016/1/10.
 */
public class Info extends Application{

    public static final String PREFERENCES_USER_INFO = "userInfo";
    public static final String PREFERENCES_SETTING = "setting";

    private HashMap<String, List<ArticlesListItem	>> articleListContainer = new HashMap<>();
    private List<Grade> gradeContainer = new ArrayList<>();
    private List<Course> courseContainer = new ArrayList<>();

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

    public HashMap<String, List<ArticlesListItem>> getArticleListContainer() {
        return articleListContainer;
    }

    public void setArticleListContainer(HashMap<String, List<ArticlesListItem>> articleListContainer) {
        this.articleListContainer = articleListContainer;
    }

    public List<Course> getCourseContainer() {
        return courseContainer;
    }

    public void setCourseContainer(List<Course> courseContainer) {
        this.courseContainer = courseContainer;
    }

    public List<Grade> getGradeContainer() {
        return gradeContainer;
    }

    public void setGradeContainer(List<Grade> gradeContainer) {
        this.gradeContainer = gradeContainer;
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
