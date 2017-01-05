package com.xybst.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.xybst.bean.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 创宇 on 2016/1/8.
 */
public class CourseDAO {

    public static final String TB_NAME = "courseDatabase";

    private CourseDBHelper dbHelper;
    private SQLiteDatabase db;

    public CourseDAO(Context context) {
        dbHelper = new CourseDBHelper(context, TB_NAME, null, 1);
        db = dbHelper.getWritableDatabase();
    }

    public boolean addAllCourses(List<Course> courses) {
        boolean isSucceed = true;
        for(Course course: courses) {
            ContentValues values = new ContentValues();
            values.put(Course.COURSE_ID, course.getCourseId());
            values.put(Course.COURSE_NAME, course.getCourseName());
            values.put(Course.TEACHER_NAME, course.getTeacherName());
            values.put(Course.CLASSROOM, course.getClassroom());
            values.put(Course.DAY_OF_WEEK, course.getDayOfWeek());
            values.put(Course.START_WEEK, course.getStartWeek());
            values.put(Course.END_WEEK, course.getEndWeek());
            values.put(Course.START_SECTION, course.getStartSection());
            values.put(Course.END_SECTION, course.getEndSection());
            if(db.insert(TB_NAME, null, values) == -1)
                isSucceed = false;
        }
        return isSucceed;
    }

    /**
     * 添加课程信息(手动添加)
     */
    public long addCourse(String classroom, int courseId, String courseName, int dayOfWeek,
                    int endSection, int endWeek, int startSection, int startWeek, String teacherName) {
        Log.i("courseId", String.valueOf(courseId));
        ContentValues values = new ContentValues();
        values.put(Course.COURSE_ID, courseId);
        values.put(Course.COURSE_NAME, courseName);
        values.put(Course.TEACHER_NAME, teacherName);
        values.put(Course.CLASSROOM, classroom);
        values.put(Course.DAY_OF_WEEK, dayOfWeek);
        values.put(Course.START_WEEK, startWeek);
        values.put(Course.END_WEEK, endWeek);
        values.put(Course.START_SECTION, startSection);
        values.put(Course.END_SECTION, endSection);
        return db.insert(TB_NAME, null, values);
    }

    /**
     * 查询当前表的总行数
     */
    public long getCoursesCount() {
        String sql = "SELECT COUNT(*) FROM " + TB_NAME;
        SQLiteStatement statement = db.compileStatement(sql);
        return statement.simpleQueryForLong();
    }

    /**
     * 返回所有课程
     */
    public List<Course> getAllCourses() {
        Cursor cursor = db.query(TB_NAME, null, null, null, null, null, "courseId ASC");
        try {
            List<Course> courses = new ArrayList<>();
            while (cursor.moveToNext()) {
                courses.add(getCourse(cursor));
            }
            return courses;
        } finally {
            cursor.close();
        }
    }

    public Course getCourse(Cursor cursor) {
        Course course = new Course();
        course.setCourseId(cursor.getInt(cursor.getColumnIndex(Course.COURSE_ID)));
        course.setCourseName(cursor.getString(cursor.getColumnIndex(Course.COURSE_NAME)));
        course.setTeacherName(cursor.getString(cursor.getColumnIndex(Course.TEACHER_NAME)));
        course.setClassroom(cursor.getString(cursor.getColumnIndex(Course.CLASSROOM)));
        course.setDayOfWeek(cursor.getInt(cursor.getColumnIndex(Course.DAY_OF_WEEK)));
        course.setStartWeek(cursor.getInt(cursor.getColumnIndex(Course.START_WEEK)));
        course.setEndWeek(cursor.getInt(cursor.getColumnIndex(Course.END_WEEK)));
        course.setStartSection(cursor.getInt(cursor.getColumnIndex(Course.START_SECTION)));
        course.setEndSection(cursor.getInt(cursor.getColumnIndex(Course.END_SECTION)));
        return course;
    }

    private class CourseDBHelper extends SQLiteOpenHelper {

        public CourseDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                              int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " +
                    TB_NAME + "( "+Course.COURSE_ID + " integer," +
                    Course.COURSE_NAME + " varchar," +
                    Course.TEACHER_NAME + " varchar," +
                    Course.CLASSROOM + " varchar," +
                    Course.DAY_OF_WEEK + " int," +
                    Course.START_WEEK + " int," +
                    Course.END_WEEK + " int," +
                    Course.START_SECTION + " int," +
                    Course.END_SECTION + " int" +
                    ")");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
            onCreate(db);
        }
    }
}