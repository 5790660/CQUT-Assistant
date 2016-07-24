package com.xybst.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;

import com.xybst.activity.R;

/**
 * Created by 创宇 on 2015/12/23.
 */
public class CourseView extends Button{

    private int courseId;
    private int startSection;
    private int endSection;
    private int dayOfWeek;

    public CourseView(Context context) {
        this(context, null);
    }

    public CourseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CourseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.CourseView);
        courseId = array.getInt(R.styleable.CourseView_courseId, 0);
        startSection=array.getInt(R.styleable.CourseView_startSection, 0);
        endSection=array.getInt(R.styleable.CourseView_endSection, 0);
        dayOfWeek=array.getInt(R.styleable.CourseView_dayOfWeek, 0);
        array.recycle();
    }

    public int getEndSection() {
        return endSection;
    }

    public void setEndSection(int endSection) {
        this.endSection = endSection;
    }

    public int getStartSection() {
        return startSection;
    }

    public void setStartSection(int startSection) {
        this.startSection = startSection;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

}
