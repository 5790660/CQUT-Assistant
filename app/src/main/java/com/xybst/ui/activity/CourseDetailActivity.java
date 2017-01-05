package com.xybst.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.xybst.activity.R;
import com.xybst.bean.Course;
import com.xybst.util.CacheLoader;
import com.xybst.util.Info;

public class CourseDetailActivity extends AppCompatActivity {

    private View.OnClickListener navListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CourseDetailActivity.this.finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow_back);
        toolbar.setNavigationOnClickListener(navListener);

        Intent intent = getIntent();
        int courseId = intent.getIntExtra("courseId", 1);
        Info info = (Info) getApplication();
        int size = CacheLoader.getCourseCtr().size();
        for (int i = 0; i < size ; i++) {
            Course course = CacheLoader.getCourseCtr().get(i);
            if (courseId == course.getCourseId()) {
                ((TextView) findViewById(R.id.courseName)).setText(course.getCourseName());
                ((TextView) findViewById(R.id.classroom)).setText(course.getClassroom());
                ((TextView) findViewById(R.id.teacher)).setText(course.getTeacherName());
                ((TextView) findViewById(R.id.section)).setText("周" + course.getDayOfWeek() + " " + course.getStartSection() + "-" + course.getEndSection() + "节");
                ((TextView) findViewById(R.id.week)).setText(course.getStartWeek() + "-" + course.getEndWeek() + "周");
                break;
            }
        }
    }
}
